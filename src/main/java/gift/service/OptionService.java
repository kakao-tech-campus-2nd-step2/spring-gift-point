package gift.service;

import gift.controller.dto.OptionRequest;
import gift.controller.dto.OptionResponse;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.utils.error.DuplicateOptionException;
import gift.utils.error.OptionNotFoundException;
import gift.utils.error.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public OptionResponse addOption(OptionRequest optionRequest) {
        Product product = productRepository.findById(optionRequest.getProductId()).orElseThrow(
            () -> new ProductNotFoundException("Product Not Found")
        );
        if (product.getOptions().stream()
            .anyMatch(o -> o.getName().equals(optionRequest.getName()))) {
            throw new DuplicateOptionException("Option name already exists for this product");
        }
        Option option = new Option(optionRequest.getName(), optionRequest.getQuantity());
        option.setProduct(product);
        product.addOption(option);
        Option save = optionRepository.save(option);

        return new OptionResponse(save.getId(), save.getName(), save.getQuantity());
    }

    @Transactional
    public Long deleteOption(Long id) {
        optionRepository.findById(id).orElseThrow(
            () -> new OptionNotFoundException("Option Not Found")
        );
        optionRepository.deleteById(id);
        return id;
    }

}
