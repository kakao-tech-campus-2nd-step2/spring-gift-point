package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.OptionDTO;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void subtractOptionQuantity(Long optionId, int quantity) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new IllegalArgumentException("Option not found"));
        option.subtractQuantity(quantity);
        optionRepository.save(option);
    }

    @Transactional
    public OptionDTO addOption(Long productId, OptionDTO optionDTO) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        validateOptionNames(product, optionDTO.getName());

        Option option = optionDTO.toEntity(product);
        Option savedOption = optionRepository.save(option);
        return OptionDTO.from(savedOption);
    }

    @Transactional(readOnly = true)
    public List<OptionDTO> getOptionsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return product.getOptions().stream()
            .map(OptionDTO::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void validateProductOptions(List<Option> options) {
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("A product must have at least one option.");
        }
        for (Option option : options) {
            if (options.stream().filter(opt -> opt.getName().equals(option.getName())).count() > 1) {
                throw new IllegalArgumentException("Option names must be unique within the same product.");
            }
        }
    }

    private void validateOptionNames(Product product, String optionName) {
        for (Option option : product.getOptions()) {
            if (option.getName().equals(optionName)) {
                throw new IllegalArgumentException("Option name must be unique within the same product.");
            }
        }
    }
}
