package gift.product;

import static gift.exception.ErrorMessage.CATEGORY_NOT_FOUND;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;

import gift.category.Category;
import gift.category.CategoryRepository;
import gift.product.dto.ProductRequestDTO;
import gift.product.dto.ProductResponseDTO;
import gift.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
        ProductRepository productRepository,
        CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
            .stream()
            .map(product -> new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory()
            ))
            .toList();
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
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
