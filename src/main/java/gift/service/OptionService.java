package gift.service;

import gift.dto.OptionDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<Option> getOptionsByProductId(long productId) {
        return optionRepository.findByProductId(productId);
    }

    @Transactional
    public Option addOptionToProduct(Long productId, OptionDTO optionDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        // redundancy option check
        Optional<Option> existingOption = optionRepository.findByProductIdAndName(productId, optionDTO.getName());
        if (existingOption.isPresent()) {
            throw new RuntimeException("Duplicate option name");
        }

        Option option = new Option();
        option.update(optionDTO.getName(), optionDTO.getQuantity());
        optionRepository.save(option);

        product.getOptions().add(option);
        return optionRepository.save(option);
    }

    // Stay until use this method.
    public void decreseOptionQuantity(Long optionId, Long quantity) {
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new RuntimeException("Option not found"));

        if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if(option.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough quantity available");
        }

        option.setQuantity(option.getQuantity() - quantity);
        optionRepository.save(option);
    }

}
