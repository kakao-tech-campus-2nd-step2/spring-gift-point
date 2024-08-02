package gift.domain.service;

import gift.domain.dto.request.OptionAddRequest;
import gift.domain.dto.request.OptionUpdateRequest;
import gift.domain.dto.response.OptionDetailedResponse;
import gift.domain.entity.Option;
import gift.domain.entity.Product;
import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;
import gift.domain.repository.OptionRepository;
import gift.global.WebConfig.Constants.Domain;
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
        return optionRepository.findById(id).orElseThrow(() -> new ServerException(ErrorCode.OPTION_NOT_FOUND));
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

        option.set(request);
        return option;
    }

    @Transactional
    public void deleteOptionById(Product product, Long optionId) {
        validateOptionIsInProduct(product, optionId);
        Option option = getOptionById(optionId);
        product.getOptions().remove(option);
        optionRepository.delete(option);
    }

    @Transactional
    public Option subtractQuantity(Option option, Integer quantity) {
        int updatedQuantity = option.getQuantity() - quantity;
        if (updatedQuantity < Domain.Option.QUANTITY_RANGE_MIN) {
            throw new ServerException(ErrorCode.OPTION_QUANTITY_OUT_OF_RANGE);
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
            throw new ServerException(ErrorCode.OPTION_ALREADY_EXISTS_IN_PRODUCT);
        }
    }

    // request가 기존 상품 옵션들과 겹치지 않음을 검증하기
    private void validateOptionIsUniqueInProduct(Product product, String optionName) {
        Objects.requireNonNullElse(product.getOptions(), new ArrayList<Option>()).stream()
            .filter(o -> o.getName().equals(optionName))
            .findAny()
            .ifPresent(o -> {
                throw new ServerException(ErrorCode.OPTION_ALREADY_EXISTS_IN_PRODUCT);
            });
    }

    // 옵션이 상품에 포함되어 있음을 검증하기
    private void validateOptionIsInProduct(Product product, Long optionId) {
        product.getOptions().stream()
            .filter(o -> o.getId().equals(optionId))
            .findAny()
            .orElseThrow(() -> new ServerException(ErrorCode.OPTION_NOT_INCLUDED_IN_PRODUCT_OPTIONS));
    }
}
