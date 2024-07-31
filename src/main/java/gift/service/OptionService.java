package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.option.CreateOptionRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.UpdateOptionRequest;
import gift.exception.product.ProductCustomException.ProductMustHaveOptionException;
import gift.repository.OptionRepository;
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
        List<Option> options = optionRepository.findAllByProductId(productId);
        return options.stream()
                .map(o -> new OptionResponse(o.getId(), o.getName(), o.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addOption(Product product, CreateOptionRequest createOptionRequest) {
        Option option = new Option(createOptionRequest.name(), createOptionRequest.quantity(), product);
        optionRepository.save(option);
    }

    @Transactional
    public void addOptionList(Product product, List<CreateOptionRequest> createOptionRequests) {
        if (createOptionRequests.isEmpty()) {
            throw new ProductMustHaveOptionException();
        }

        for (CreateOptionRequest request : createOptionRequests) {
            addOption(product, request);
        }
    }

    @Transactional
    public void updateOption(Product product, Long optionId, UpdateOptionRequest updateOptionRequest) {
        Option option = getOption(optionId);
        checkOptionOwner(product, option);
        option.updateOption(updateOptionRequest.name(), updateOptionRequest.quantity());
        optionRepository.save(option);
    }

    @Transactional
    public void deleteOption(Product product, Long optionId) {
        Option option = getOption(optionId);
        checkOptionOwner(product, option);
        if (optionRepository.countByProductId(product.getId()) <= 1) {
            throw new ProductMustHaveOptionException();
        }
        optionRepository.delete(option);
    }

    @Transactional(readOnly = true)
    public Option getOption(Long optionId) {
        Optional<Option> option = optionRepository.findById(optionId);
        return option.orElseThrow(() -> new EntityNotFoundException("Option"));
    }

    private void checkOptionOwner(Product product, Option option) {
        if (!option.isOwner(product.getId())) {
            throw new EntityNotFoundException("Option");
        }
    }

    @Transactional
    public Option subtractOptionQuantity(Long optionId, int quantity) {
        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new EntityNotFoundException("Option"));

        option.subtract(quantity);
        return optionRepository.save(option);
    }
}
