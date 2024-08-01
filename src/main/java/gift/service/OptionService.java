package gift.service;

import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<OptionResponse> findByProductId(Long productId) {
        List<Option> options = optionRepository.findByProductId(productId);
        return options.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public OptionResponse save(OptionRequest optionRequest) {
        Product product = productRepository.findById(optionRequest.productId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        Option option = new Option(optionRequest.name(), optionRequest.quantity(), product);
        Option savedOption = optionRepository.save(option);
        return convertToResponse(savedOption);
    }

    private OptionResponse convertToResponse(Option option) {
        return new OptionResponse(option.getId(), option.getName(), option.getQuantity());
    }

    public void subtractQuantity(Long optionId, int quantity) {
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Option not found"));
        option.subtractQuantity(quantity);
        optionRepository.save(option);
    }
}
