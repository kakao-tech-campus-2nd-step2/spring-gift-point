package gift.product.application;

import gift.category.domain.Category;
import gift.category.domain.CategoryRepository;
import gift.exception.type.NotFoundException;
import gift.option.application.OptionServiceResponse;
import gift.option.application.command.OptionCreateCommand;
import gift.option.domain.Option;
import gift.option.domain.OptionRepository;
import gift.product.application.command.ProductCreateCommand;
import gift.product.application.command.ProductUpdateCommand;
import gift.product.domain.Product;
import gift.product.domain.ProductRepository;
import gift.wishlist.domain.WishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public ProductService(
            ProductRepository productRepository,
            WishlistRepository wishlistRepository,
            CategoryRepository categoryRepository,
            OptionRepository optionRepository
    ) {
        this.productRepository = productRepository;
        this.wishlistRepository = wishlistRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public Page<ProductServiceResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductServiceResponse::from);
    }

    public ProductServiceResponse findById(Long productId) {
        return productRepository.findById(productId)
                .map(ProductServiceResponse::from)
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));
    }

    @Transactional
    public void save(ProductCreateCommand command) {
        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new NotFoundException("해당 카테고리가 존재하지 않습니다."));

        Product product = command.toProduct(category);
        product.validateKakaoInProductName();

        command.optionCreateCommandList().stream()
                .map(OptionCreateCommand::toOption)
                .forEach(product::addOption);

        product.validateHasAtLeastOneOption();

        productRepository.save(product);
    }

    @Transactional
    public void update(ProductUpdateCommand command) {
        Product product = productRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));

        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new NotFoundException("해당 카테고리가 존재하지 않습니다."));

        product.update(category, command);
        updateOptions(command, product);

        product.validateHasAtLeastOneOption();
        product.validateKakaoInProductName();
    }

    private void updateOptions(ProductUpdateCommand command, Product product) {
        command.optionUpdateCommandList().forEach(
                optionUpdateCommand -> {
                    Option originalOption = optionRepository.findById(optionUpdateCommand.id())
                            .orElseThrow(() -> new NotFoundException("해당 옵션이 존재하지 않습니다."));
                    originalOption.update(optionUpdateCommand.name(), optionUpdateCommand.quantity(), product);
                }
        );
    }

    @Transactional
    public void delete(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("해당 상품이 존재하지 않습니다."));

        wishlistRepository.deleteAllByProductId(productId);
        productRepository.delete(product);
    }

    public List<OptionServiceResponse> findOptionsByProductId(Long productId) {
        return optionRepository.findAllByProductId(productId)
                .stream()
                .map(OptionServiceResponse::from)
                .toList();
    }
}
