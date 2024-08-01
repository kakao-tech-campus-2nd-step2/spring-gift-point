package gift.repository;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    public void setUp() {
        testCategory = new Category();
        testCategory.setName("Test Category");
        testCategory.setColor("#FFFFFF");
        testCategory.setImageUrl("http://example.com/image.jpg");
        testCategory.setDescription("This is a test category.");
        categoryRepository.save(testCategory);

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(1000);
        testProduct.setImageUrl("http://example.com/product_image.jpg");
        testProduct.setCategory(testCategory);

        Option option1 = new Option();
        option1.setName("Option 1");
        option1.setQuantity(10);
        option1.setProduct(testProduct);

        Option option2 = new Option();
        option2.setName("Option 2");
        option2.setQuantity(20);
        option2.setProduct(testProduct);

        testProduct.setOptions(Arrays.asList(option1, option2));

        productRepository.save(testProduct);
        optionRepository.save(option1);
        optionRepository.save(option2);
    }

    @Test
    public void testFindById_Success() {
        Optional<Product> foundProduct = productRepository.findById(testProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo(testProduct.getName());
    }

    @Test
    public void testFindByCategoryId_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Product> products = productRepository.findAllByCategoryId(testCategory.getId(), pageable).getContent();
        assertThat(products).isNotEmpty();
        assertThat(products.getFirst().getCategory().getId()).isEqualTo(testCategory.getId());
    }

    @Test
    public void testSaveProduct_Success() {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setPrice(2000);
        newProduct.setImageUrl("http://example.com/new_product_image.jpg");
        newProduct.setCategory(testCategory);

        Option newOption = new Option();
        newOption.setName("New Option");
        newOption.setQuantity(5);
        newOption.setProduct(newProduct);

        newProduct.setOptions(List.of(newOption));

        productRepository.save(newProduct);
        optionRepository.save(newOption);

        Optional<Product> savedProduct = productRepository.findById(newProduct.getId());
        assertThat(savedProduct).isPresent();
        assertThat(savedProduct.get().getName()).isEqualTo("New Product");
    }

    @Test
    public void testDeleteProduct_Success() {
        productRepository.deleteById(testProduct.getId());
        Optional<Product> deletedProduct = productRepository.findById(testProduct.getId());
        assertThat(deletedProduct).isNotPresent();
    }
}
