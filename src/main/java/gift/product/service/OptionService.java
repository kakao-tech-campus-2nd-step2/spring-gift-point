package gift.product.service;

import gift.product.model.OptionRepository;
import gift.product.model.dto.option.CreateOptionRequest;
import gift.product.model.dto.option.Option;
import gift.product.model.dto.option.OptionResponse;
import gift.product.model.dto.option.UpdateOptionRequest;
import gift.product.model.dto.product.Product;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {
    private final OptionRepository optionRepository;

    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }


    @Transactional(readOnly = true)
    public List<OptionResponse> findOptionsByProductId(Long productId) {
        List<Option> options = optionRepository.findAllByProductIdAndIsActiveTrue(productId);
        return options.stream()
                .map(o -> new OptionResponse(o.getId(), o.getName(), o.getQuantity(), o.getAdditionalCost()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addOption(Product product, CreateOptionRequest createOptionRequest) {
        Option option = new Option(createOptionRequest.name(), createOptionRequest.quantity(),
                createOptionRequest.additionalCost(), product);
        optionRepository.save(option);
    }

    @Transactional
    public void addOptionList(Product product, List<CreateOptionRequest> createOptionRequests) {
        for (CreateOptionRequest request : createOptionRequests) {
            addOption(product, request);
        }
    }

    @Transactional
    public void updateOption(Product product, Long optionId, UpdateOptionRequest updateOptionRequest) {
        Option option = getOption(optionId);
        checkOptionOwner(product, option);
        option.updateOption(updateOptionRequest.name(), updateOptionRequest.quantity(),
                updateOptionRequest.additionalCost());
        optionRepository.save(option);
    }

    @Transactional
    public void deleteOption(Product product, Long optionId) {
        Option option = getOption(optionId);
        checkOptionOwner(product, option);
        if (optionRepository.countByProductIdAndIsActiveTrue(product.getId()) <= 1) {
            throw new IllegalStateException("상품은 하나 이상의 옵션을 가지고 있어야 합니다.");
        }
        option.inactive();
        optionRepository.save(option);
    }

    @Transactional(readOnly = true)
    public Option getOption(Long optionId) {
        Optional<Option> option = optionRepository.findByIdAndIsActiveTrue(optionId);
        return option.orElseThrow(() -> new EntityNotFoundException("Option"));
    }

    private void checkOptionOwner(Product product, Option option) {
        if (!option.isOwner(product.getId())) {
            throw new EntityNotFoundException("Option");
        }
    }
}
