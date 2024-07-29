package gift.Product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.service.CategoryService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private static final String CATEGORY_NAME = "교환권";
    private static final String PRODUCT_NAME = "카푸치노";
    private static final String PRODUCT_NAME_2 = "초코라떼";
    private static final String PRODUCT_NAME_3 = "그린티라떼";
    private static final int PRODUCT_PRICE = 3000;
    private static final int PRODUCT_PRICE_2 = 3500;
    private static final String PRODUCT_URL = "example.com";
    private static final String PRODUCT_URL_2 = "example2.com";
    private static final String PRODUCT_URL_3 = "example3.com";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Category category = new Category(CATEGORY_NAME);
        category.setId(1L);

        when(categoryRepository.findByCategoryName(CATEGORY_NAME)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
    }

    @Test
    void testFindById() {
        Category category = categoryRepository.findByCategoryName(CATEGORY_NAME).orElseThrow();
        Product expected = new Product(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_URL, category);
        expected.setId(1L);

        when(productRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(productRepository.save(any(Product.class))).thenReturn(expected);

        productRepository.save(expected);
        Optional<Product> actual = productRepository.findById(expected.getId());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getName()).isEqualTo(PRODUCT_NAME);
    }

    @Test
    void testSaveProduct() {
        Category category = categoryRepository.findByCategoryName(CATEGORY_NAME).orElseThrow();
        Product expected = new Product(PRODUCT_NAME_2, PRODUCT_PRICE_2, PRODUCT_URL_2, category);
        expected.setId(2L);

        when(productRepository.save(any(Product.class))).thenReturn(expected);

        Product actual = productRepository.save(expected);

        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    void testDeleteProduct() {
        Category category = categoryRepository.findByCategoryName(CATEGORY_NAME).orElseThrow();
        Product expected = new Product(PRODUCT_NAME_3, PRODUCT_PRICE_2, PRODUCT_URL_3, category);
        expected.setId(3L);

        when(productRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        productRepository.save(expected);
        productRepository.deleteById(expected.getId());

        when(productRepository.findById(expected.getId())).thenReturn(Optional.empty());

        Optional<Product> actual = productRepository.findById(expected.getId());

        assertThat(actual).isNotPresent();
    }
}