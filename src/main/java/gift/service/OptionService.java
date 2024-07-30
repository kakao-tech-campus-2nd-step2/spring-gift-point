package gift.service;

import gift.dto.OptionDto;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    public List<Option> getOptionsByProductId(Long productId) {
        Product product = findProductById(productId);
        return product.getOptions();
    }

    public Option addOption(Long productId, OptionDto optionDto) {
        Product product = findProductById(productId);
        validateDuplicateOptionName(product, optionDto.getName());
        Option option = new Option(optionDto.getName(), optionDto.getQuantity());
        optionRepository.save(option);
        product.getOptions().add(option);
        productRepository.save(product);
        return option;
    }

    public Option updateOption(Long optionId, OptionDto optionDto) {
        Product product = findProductByOptionId(optionId);
        validateDuplicateOptionName(product, optionDto.getName(), optionId);
        Option updatedOption = new Option(optionId, optionDto.getName(), optionDto.getQuantity());
        optionRepository.save(updatedOption);
        return updatedOption;
    }

    @Transactional
    public void deleteOption(Long optionId) {
        Option option = findOptionById(optionId);
        Product product = findProductByOptionId(optionId);
        if (product.getOptions().size() <= 1) {
            throw new IllegalArgumentException("상품에는 최소 하나의 옵션이 있어야 합니다");
        }
        product.getOptions().remove(option);
        productRepository.save(product);
        optionRepository.deleteById(optionId);
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 상품입니다."));
    }

    public Product findProductByOptionId(Long optionId) {
        return optionRepository.findProductByOptionId(optionId)
                .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않는 옵션입니다."));
    }

    public Option findOptionById(Long optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 옵션입니다."));
    }

    private void validateDuplicateOptionName(Product product, String optionName) {
        boolean isDuplicate = product.getOptions().stream()
                .anyMatch(option -> option.getName().equals(optionName));
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 옵션 이름입니다.");
        }
    }

    private void validateDuplicateOptionName(Product product, String optionName, Long optionId) {
        boolean isDuplicate = product.getOptions().stream()
                .anyMatch(option -> option.getName().equals(optionName) && !option.getId().equals(optionId));
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 옵션 이름입니다.");
        }
    }

    @Transactional
    public Option decreaseOptionQuantity(Long optionId, int amount) {
        Option option = findOptionById(optionId);
        if (option.getQuantity() <= amount) {
            throw new IllegalArgumentException("옵션의 수량은 최소 1개 이상이어야 합니다.");
        }
        Option decreasedoption = new Option(option.getId(), option.getName(), option.getQuantity() - amount);
        return optionRepository.save(decreasedoption);
    }
}
