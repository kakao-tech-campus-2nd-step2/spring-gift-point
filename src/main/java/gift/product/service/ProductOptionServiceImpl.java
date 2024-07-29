package gift.product.service;

import gift.core.domain.product.ProductOption;
import gift.core.domain.product.ProductOptionRepository;
import gift.core.domain.product.ProductOptionService;
import gift.core.domain.product.ProductRepository;
import gift.core.domain.product.exception.*;
import gift.core.exception.ErrorCode;
import gift.core.exception.validation.InvalidArgumentException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductOptionServiceImpl implements ProductOptionService {
    @Value("${app.product.max-option}") Long maxOptionLimit;
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductOptionServiceImpl(
            ProductOptionRepository productOptionRepository,
            ProductRepository productRepository
    ) {
        this.productOptionRepository = productOptionRepository;
        this.productRepository = productRepository;
    }

    public ProductOptionServiceImpl(
            ProductOptionRepository productOptionRepository,
            ProductRepository productRepository,
            Long maxOptionLimit
    ) {
        this.productOptionRepository = productOptionRepository;
        this.productRepository = productRepository;
        this.maxOptionLimit = maxOptionLimit;
    }

    @Override
    @Transactional
    public void registerOptionToProduct(Long productId, ProductOption productOption) {
        if (!productRepository.exists(productId)) {
            throw new ProductNotFoundException();
        }
        if (productOptionRepository.hasOption(productOption.id())) {
            throw new OptionAlreadExistsException();
        }
        if (productOptionRepository.hasOption(productId, productOption.name())) {
            throw new OptionAlreadExistsException();
        }
        if (productOptionRepository.countByProductId(productId) >= maxOptionLimit) {
            throw new OptionLimitExceededException();
        }
        if (productOption.quantity() <= 0) {
            throw new InvalidArgumentException(ErrorCode.NEGATIVE_QUANTITY);
        }
        productOptionRepository.save(productId, productOption);
    }

    @Override
    @Transactional
    public List<ProductOption> getOptionsFromProduct(Long productId) {
        if (!productRepository.exists(productId)) {
            throw new ProductNotFoundException();
        }
        return productOptionRepository.findAllByProductId(productId);
    }

    @Override
    @Transactional
    public void removeOptionFromProduct(Long productId, Long optionId) {
        if (!productRepository.exists(productId)) {
            throw new ProductNotFoundException();
        }
        if (!productOptionRepository.hasOption(optionId)) {
            throw new OptionNotFoundException();
        }

        productOptionRepository.deleteById(optionId);
    }

    @Override
    @Transactional
    public void subtractQuantityFromOption(Long optionId, Integer quantity) {
        ProductOption option = productOptionRepository
                .findById(optionId)
                .orElseThrow(OptionNotFoundException::new);
        option.validateOrderable(quantity);
        productOptionRepository.save(
                optionId,
                option.applyDecreaseQuantity(quantity)
        );
    }
}
