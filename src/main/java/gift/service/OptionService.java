package gift.service;

import gift.DTO.option.OptionRequest;
import gift.DTO.option.OptionResponse;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;
    @Autowired
    public OptionService(
        OptionRepository optionRepository,
        ProductRepository productRepository
    ) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<OptionResponse> getAllOptionsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("No such product with id" + productId));

        List<OptionResponse> responses = product.getOptions().stream()
                                .map(OptionResponse::fromEntity)
                                .toList();
        return  responses;
    }

    @Transactional
    public OptionResponse addOption(Long productId, OptionRequest optionRequest) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new RuntimeException("No such product with id" + productId)
        );

        validateOptionNameNotDuplicated(productId, optionRequest);

        Option option = new Option(optionRequest.name(), optionRequest.quantity(), product);
        product.addOption(option);
        optionRepository.save(option);
        return OptionResponse.fromEntity(option);
    }

    @Transactional
    public OptionResponse decrementOptionQuantity(Long optionId, Long quantity) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new RuntimeException("No such option with id " + optionId));

        option.subtract(quantity);
        Option savedOption = optionRepository.save(option);
        return OptionResponse.fromEntity(savedOption);
    }

    private void validateOptionNameNotDuplicated(Long productId, OptionRequest optionRequest) {
        boolean exists = optionRepository.existsByProductIdAndName(productId, optionRequest.name());
        if (exists) {
            throw new RuntimeException(
                String.format("Option with name %s already exists for product %d",
                    optionRequest.name(), productId)
            );
        }
    }

    @Transactional(readOnly = true)
    protected Option getOptionById(Long optionId) {
        return optionRepository.findById(optionId)
            .orElseThrow(() -> new RuntimeException("No such option" + optionId));
    }
}
