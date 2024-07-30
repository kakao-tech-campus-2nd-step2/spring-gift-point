package gift.service.impl;

import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.service.OptionService;
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
