package gift.domain.service;

import gift.domain.dto.request.OptionAddRequest;
import gift.domain.dto.request.OptionUpdateRequest;
import gift.domain.dto.response.OptionDetailedResponse;
import gift.domain.entity.Option;
import gift.domain.entity.Product;
import gift.domain.exception.badRequest.OptionQuantityOutOfRangeException;
import gift.domain.exception.badRequest.OptionUpdateActionInvalidException;
import gift.domain.exception.conflict.OptionAlreadyExistsInProductException;
import gift.domain.exception.notFound.OptionNotFoundException;
import gift.domain.exception.notFound.OptionNotIncludedInProductOptionsException;
import gift.domain.repository.OptionRepository;
import gift.global.WebConfig.Constants.Domain;
import gift.global.WebConfig.Constants.Domain.Option.QuantityUpdateAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Transactional(readOnly = true)
    public List<OptionDetailedResponse> getAllOptions() {
        return OptionDetailedResponse.of(optionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Option getOptionById(Long id) {
        return optionRepository.findById(id).orElseThrow(OptionNotFoundException::new);
    }

    @Transactional
    public Option addOption(Product product, OptionAddRequest request) {
        validateOptionIsUniqueInProduct(product, request.name());
        Option option = optionRepository.save(request.toEntity(product));
        product.addOption(option);
        return option;
    }

    @Transactional
    public List<Option> addOptions(Product product, List<OptionAddRequest> request) {
        validateOptionNamesDistinct(product.getOptions(), request);
        List<Option> options = request.stream().map(req -> req.toEntity(product)).toList();
        optionRepository.saveAll(options);
        product.addOptions(options);
        return optionRepository.findAllByProduct(product);
    }

    @Transactional
    public Option updateOptionById(Product product, Long optionId, OptionUpdateRequest request) {
        validateOptionIsInProduct(product, optionId);
        Option option = getOptionById(optionId);

        if (!option.getName().equals(request.name())) {
            validateOptionIsUniqueInProduct(product, request.name());
        }

        if (request.action().equals(QuantityUpdateAction.ADD.toString())) {
            addQuantity(option, request.quantity());
            option.setName(request.name());
            return option;
        }

        if (request.action().equals(QuantityUpdateAction.SUBTRACT.toString())) {
            subtractQuantity(option, request.quantity());
            option.setName(request.name());
            return option;

        }

        throw new OptionUpdateActionInvalidException();
    }

    @Transactional
    public void deleteOptionById(Product product, Long optionId) {
        validateOptionIsInProduct(product, optionId);
        Option option = getOptionById(optionId);
        product.getOptions().remove(option);
        optionRepository.delete(option);
    }

    @Transactional
    public Option addQuantity(Option option, Integer quantity) {
        int updatedQuantity = option.getQuantity() + quantity;
        if (updatedQuantity > Domain.Option.QUANTITY_RANGE_MAX) {
            throw new OptionQuantityOutOfRangeException();
        }
        option.add(quantity);
        return option;
    }

    @Transactional
    public Option subtractQuantity(Option option, Integer quantity) {
        int updatedQuantity = option.getQuantity() - quantity;
        if (updatedQuantity < Domain.Option.QUANTITY_RANGE_MIN) {
            throw new OptionQuantityOutOfRangeException();
        }
        option.subtract(quantity);
        return option;
    }

    //옵션 이름들이 중복이 없는지 검증
    private void validateOptionNamesDistinct(List<Option> productOptions, List<OptionAddRequest> request) {
        //프로덕트 옵션 이름 리스트, 요청 옵션 이름 리스트를 합친 뒤 distinct 처리로 중복을 제거
        List<String> optionNames = new ArrayList<>(productOptions.stream()
            .map(Option::getName)
            .toList());
        optionNames.addAll(request.stream()
            .map(OptionAddRequest::name)
            .toList());
        List<String> distinctOptionNames = optionNames.stream().distinct().toList();

        //이름 중복을 제거한 리스트 길이가 원래 두 옵션이름 리스트 길이 합보다 줄어들었으면 중복이 존재함
        if (distinctOptionNames.size() < productOptions.size() + request.size()) {
            throw new OptionAlreadyExistsInProductException();
        }
    }

    // request가 기존 상품 옵션들과 겹치지 않음을 검증하기
    private void validateOptionIsUniqueInProduct(Product product, String optionName) {
        Objects.requireNonNullElse(product.getOptions(), new ArrayList<Option>()).stream()
            .filter(o -> o.getName().equals(optionName))
            .findAny()
            .ifPresent(o -> {
                throw new OptionAlreadyExistsInProductException();
            });
    }

    // 옵션이 상품에 포함되어 있음을 검증하기
    private void validateOptionIsInProduct(Product product, Long optionId) {
        product.getOptions().stream()
            .filter(o -> o.getId().equals(optionId))
            .findAny()
            .orElseThrow(OptionNotIncludedInProductOptionsException::new);
    }
}
