package gift.product.service;

import gift.product.domain.ProductOption;
import gift.product.exception.option.ProductOptionDuplicatedException;
import gift.product.exception.option.ProductOptionNotDeletedException;
import gift.product.exception.option.ProductOptionNotFoundException;
import gift.product.exception.product.ProductNotFoundException;
import gift.product.persistence.ProductOptionRepository;
import gift.product.persistence.ProductRepository;
import gift.product.service.command.ProductOptionCommand;
import gift.product.service.dto.ProductOptionInfo;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductOptionService {
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    public ProductOptionService(ProductRepository productRepository, ProductOptionRepository productOptionRepository) {
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
    }

    @Transactional
    public Long createProductOption(final Long productId, ProductOptionCommand productOptionCommand) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> ProductNotFoundException.of(productId));
        validateDuplicatedProductName(productId, List.of(productOptionCommand.name()));

        var productOption = productOptionCommand.toEntity(product);

        var savedProduct = productOptionRepository.save(productOption);
        return savedProduct.getId();
    }

    @Transactional
    public void createProductOptions(final Long productId, final List<ProductOptionCommand> productOptionCommand) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> ProductNotFoundException.of(productId));

        var names = productOptionCommand.stream().map(ProductOptionCommand::name).toList();
        validateDuplicatedProductName(productId, names);

        List<ProductOption> productOptions = productOptionCommand.stream()
                .map(productOptionRequest -> productOptionRequest.toEntity(product))
                .toList();
        //배치처리가 안됨.
        productOptionRepository.saveAll(productOptions);
    }

    @Transactional
    public void modifyProductOption(Long productId, Long optionId, ProductOptionCommand productOptionCommand) {
        var productOption = getExistsProductOptionForUpdate(productId, optionId);

        productOption.modify(productOptionCommand.name(), productOptionCommand.quantity());
    }

    @Transactional(readOnly = true)
    public ProductOptionInfo getProductOptionInfo(Long productId, Long optionId) {
        var productOption = getExistsProductOption(productId, optionId);

        return ProductOptionInfo.from(productOption);
    }

    @Transactional(readOnly = true)
    public List<ProductOptionInfo> getAllProductOptions(Long productId) {
        var productOptions = productOptionRepository.findByProductId(productId);

        return productOptions.stream()
                .map(ProductOptionInfo::from)
                .toList();
    }

    @Transactional
    public void deleteProductOption(Long productId, Long optionId) {
        var productOption = productOptionRepository.findByProductId(productId);
        if (productOption.size() == 1) {
            throw ProductOptionNotDeletedException.of(productId, optionId);
        }

        var option = getExistsProductOption(productId, optionId);
        productOptionRepository.delete(option);
    }

    @Transactional
    public void buyProduct(final Long productId, final Long optionId, final int quantity) {
        var productOption = getExistsProductOptionForUpdate(productId, optionId);

        productOption.buy(quantity);
    }

    private ProductOption getExistsProductOption(Long productId, Long optionId) {
        var option = productOptionRepository.findByProductIdAndId(productId, optionId)
                .orElseThrow(() -> ProductOptionNotFoundException.of(productId, optionId));

        return option;
    }

    private ProductOption getExistsProductOptionForUpdate(Long productId, Long optionId) {

        return productOptionRepository.findByProductIdAndIdForUpdate(productId, optionId)
                .orElseThrow(() -> ProductOptionNotFoundException.of(productId, optionId));
    }

    private void validateDuplicatedProductName(Long productId, List<String> names) {
        var productOptions = productOptionRepository.findByProductId(productId);

        names.forEach(name -> {
            if (productOptions.stream().anyMatch(option -> option.isSameName(name))) {
                throw ProductOptionDuplicatedException.of(productId, name);
            }
        });
    }
}
