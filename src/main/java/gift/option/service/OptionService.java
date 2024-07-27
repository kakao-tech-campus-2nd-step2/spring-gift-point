package gift.option.service;

import gift.option.domain.Option;
import gift.option.domain.OptionRequest;
import gift.option.repository.OptionJpaRepository;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OptionService {
    private final OptionJpaRepository optionJpaRepository;
    private final ProductRepository productRepository;
    public OptionService(OptionJpaRepository optionJpaRepository, ProductRepository productRepository) {
        this.optionJpaRepository = optionJpaRepository;
        this.productRepository = productRepository;
    }

    // 1. option create
    @Transactional
    public Option createOption(Long productId, OptionRequest optionRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 상품은 존재하지 않음 : " + productId));

        List<Option> options = optionJpaRepository.findAllByProduct(product);
        for (Option option : options) {
            if (option.getName().equals(optionRequest.getName())) {
                throw new IllegalArgumentException("[ERROR] 중복된 옵션 존재 : " + option.getName());
            }
        }

        if (optionRequest.getQuantity() < 0 || optionRequest.getQuantity() > 100000000) {
            throw new IllegalArgumentException("[ERROR] 옵션의 수량은 0 이상 1억 미만이어야 함");
        }

        Option option = new Option(optionRequest.getName(), optionRequest.getQuantity(), product);

        checkForDuplicateOption(option);

        return optionJpaRepository.save(option);
    }

    // 2. option read
    @Transactional(readOnly = true)
    public List<Option> getAllOptionByProduct(Product product) {

        return optionJpaRepository.findAllByProduct(product);
    }

    @Transactional(readOnly = true)
    public Option getOptionById(Long optionId) {
        return optionJpaRepository.findById(optionId).orElse(null);
    }

    // 3. option update
    @Transactional
    public Option updateOption(Long productId, OptionRequest optionRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 상품은 존재하지 않음 : " + productId));

        Option existingOption = optionJpaRepository.findByProduct(product)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 상품 id의 옵션은 존재하지 않음 : " + productId));

        if (optionRequest.getQuantity() < 0 || optionRequest.getQuantity() > 100000000) {
            throw new IllegalArgumentException("[ERROR] 옵션의 수량은 0 이상 1억 미만이어야 함");
        }

        existingOption.update(optionRequest.getName(), optionRequest.getQuantity());

        checkForDuplicateOption(existingOption);

        return optionJpaRepository.save(existingOption);

    }

    // 4. option delete
    @Transactional
    public Long deleteOption(Long id) {
        Option existingOption = optionJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 옵션은 존재하지 않음 : " + id));
        Long productId = existingOption.getProduct().getId();
        optionJpaRepository.delete(existingOption);
        return productId;
    }

    // 중복성 검사
    public void checkForDuplicateOption(Option option) {
        List<Option> options = optionJpaRepository.findAll();
        for (Option o : options) {
            if (o.equals(option)) {
                throw new IllegalArgumentException("[ERROR] 중복된 옵션 존재 : " + option.getName());
            }
        }
    }
}
