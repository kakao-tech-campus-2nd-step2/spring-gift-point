package gift.product;

import gift.category.model.Category;
import gift.category.repository.CategoryRepository;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class ProductCRUDTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product exampleProduct;
    private Category exampleCategory;

    @BeforeEach
    public void setUp() {
        // given
        exampleCategory = new Category("Coffee");
        exampleProduct = new Product("Ice Americano", 2000, "http://example.com/example.jpg");
        categoryRepository.save(exampleCategory);

        // 양방향 관계 설정
        exampleProduct = new Product("Ice Americano", 2000, "http://example.com/example.jpg", exampleCategory);
        exampleCategory.addProduct(exampleProduct);

        // 저장
        productRepository.save(exampleProduct);
    }

    @Test
    public void testCreateProduct() {
        // Given
        Product product = new Product("CaffeLatte", 2500, "http://example.com/image2.jpg");

        // When
        productService.createProduct(product, exampleCategory.getId());

        // Then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2);
        assertThat(products).extracting(Product::getName).contains("CaffeLatte");
        assertThat(products).extracting(Product::getCategory).contains(exampleCategory);
    }

    @Test
    public void testUpdateProduct() {
        // Given
        Product newProduct = new Product("UpdatedProduct", 3000, "http://example.com/updated.jpg");

        // When
        productService.updateProduct(exampleProduct.getId(), newProduct, exampleCategory.getId());

        // Then
        Optional<Product> updatedProduct = productRepository.findById(exampleProduct.getId());
        assertThat(updatedProduct).isPresent();
        assertThat(updatedProduct.get().getName()).isEqualTo("UpdatedProduct");
        assertThat(updatedProduct.get().getPrice()).isEqualTo(3000);
        assertThat(updatedProduct.get().getCategory()).isEqualTo(exampleCategory);
    }

    @Test
    public void testDeleteProduct() {
        // Given
        Long productId = exampleProduct.getId();

        // When
        productService.deleteProduct(productId);

        // Then
        List<Product> products = productRepository.findAll();
        assertThat(products).isEmpty();
    }

    @Test
    public void testGetAllProducts() {
        // Given (initial setup already done in @BeforeEach)

        // When
        List<Product> products = productService.getAllProducts();

        // Then
        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("Ice Americano");
    }

    @Test
    public void testGetProductById() {
        // Given
        Long productId = exampleProduct.getId();

        // When
        Product product = productService.getProductById(productId);

        // Then
        assertThat(product).isNotNull();
        assertThat(product.getName()).isEqualTo("Ice Americano");
    }
}
