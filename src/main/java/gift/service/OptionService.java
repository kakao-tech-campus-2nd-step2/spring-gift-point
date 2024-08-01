package gift.service;

import gift.domain.Option;
import gift.domain.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public Option getOptionById(Long optionId) {
        return optionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));
    }

    public List<Option> getOptionsByProductId(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        return product.getOptions();
    }

    public void addOptionToProduct(Long productId, Option option) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));

        try {
            option.setProduct(product);
            optionRepository.save(option);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("동일한 상품 내에 동일한 옵션 이름이 존재합니다.");
        }
    }

    public void updateOption(Long productId, Long optionId, Option updatedOption) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Invalid product ID"));
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

        option.setName(updatedOption.getName());
        option.setQuantity(updatedOption.getQuantity());
        option.setProduct(product);

        try {
            optionRepository.save(option);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("동일한 상품 내에 동일한 옵션 이름이 존재합니다.");
        }
    }

    public void deleteOption(Long productId, Long optionId) {
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));
        Product product = option.getProduct();

        if (!product.getId().equals(productId)) {
            throw new IllegalArgumentException("해당 옵션이 존재하지 않습니다.");
        }

        optionRepository.delete(option);
    }

    public void subtractOptionQuantity(Long productId, String optionName, int quantity) {
        Option option = optionRepository.findByProductIdAndName(productId, optionName)
                .orElseThrow(() -> new IllegalArgumentException("해당 옵션이 존재하지 않습니다."));

        option.subtractQuantity(quantity);
        optionRepository.save(option);
    }
}
