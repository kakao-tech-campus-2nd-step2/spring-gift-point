package gift.product;

import static gift.exception.ErrorMessage.CATEGORY_NOT_FOUND;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;

import gift.category.CategoryRepository;
import gift.category.entity.Category;
import gift.option.OptionService;
import gift.product.dto.ProductPaginationResponseDTO;
import gift.product.dto.ProductRequestDTO;
import gift.product.dto.ProductResponseDTO;
import gift.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OptionService optionService;

    public ProductService(
        ProductRepository productRepository,
        CategoryRepository categoryRepository,
        OptionService optionService
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.optionService = optionService;
    }

    @Transactional(readOnly = true)
    public Page<ProductPaginationResponseDTO> getAllProducts(Pageable pageable, long categoryId) {

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException(CATEGORY_NOT_FOUND));

        return productRepository.findAllByCategory(category, pageable)
            .map(product -> new ProductPaginationResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
            ));
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND));

        return new ProductResponseDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            optionService.getOptions(productId)
        );

    }

    public void addProduct(ProductRequestDTO productDTO) {
        Category findCategory = categoryRepository.findByName(productDTO.getCategory().getName())
            .orElseThrow(() -> new IllegalArgumentException(CATEGORY_NOT_FOUND));

        productRepository.save(
            new Product(
                -1,
                productDTO.getName(),
                productDTO.getPrice(),
                productDTO.getImageUrl(),
                findCategory
            )
        );
    }

    public void updateProduct(long id, ProductRequestDTO productDTO) {
        Category findCategory = categoryRepository.findByName(productDTO.getCategory().getName())
            .orElseThrow(() -> new IllegalArgumentException(CATEGORY_NOT_FOUND));

        Product findProduct = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND));

        findProduct.update(
            productDTO.getName(),
            productDTO.getPrice(),
            productDTO.getImageUrl(),
            findCategory
        );
        productRepository.save(findProduct);
    }

    public void deleteProduct(long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(PRODUCT_NOT_FOUND));

        productRepository.delete(product);
    }
}
