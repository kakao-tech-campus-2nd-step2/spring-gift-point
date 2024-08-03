package gift.service.impl;

import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.service.OptionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OptionServiceImpl implements OptionService {

    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionServiceImpl(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Option addOptionToProduct(Long productId, Option option) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        option.setProduct(product);
        List<Option> options = optionRepository.findAllByProduct(product);
        validateOption(options, option);
        return optionRepository.save(option);
    }

    @Override
    public List<Option> getOptionsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        return optionRepository.findAllByProduct(product);
    }

    @Override
    @Transactional
    public Option updateOption(Long productId, Long optionId, Option updatedOption) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NoSuchElementException("상품이 존재하지 않습니다."));
        return optionRepository.findById(optionId)
            .map(existingOption -> {
                existingOption.setName(updatedOption.getName());
                existingOption.setQuantity(updatedOption.getQuantity());
                existingOption.setProduct(product);
                return optionRepository.save(existingOption);
            })
            .orElseThrow(() -> new NoSuchElementException("옵션이 존재하지 않습니다."));
    }

    @Override
    @Transactional
    public void deleteOption(Long productId, Long optionId) {
        if (!productRepository.existsById(productId)) {
            throw new NoSuchElementException("상품이 존재하지 않습니다.");
        }
        if (!optionRepository.existsById(optionId)) {
            throw new NoSuchElementException("옵션이 존재하지 않습니다.");
        }
        optionRepository.deleteById(optionId);
    }

    @Transactional
    public void subtractOptionQuantity(Long optionId, int quantity) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NoSuchElementException("옵션이 존재하지 않습니다."));
        option.subtractQuantity(quantity);
        optionRepository.save(option);
    }

    private void validateOption(List<Option> options, Option newOption) {
        for (Option option : options) {
            if (option.getName().equals(newOption.getName())) {
                throw new IllegalArgumentException("옵션 이름이 중복됩니다: " + newOption.getName());
            }
        }
    }
}