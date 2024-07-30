package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.category.CategoryRepository;
import gift.category.model.Category;
import gift.option.OptionRepository;
import gift.option.model.Option;
import gift.option.model.OptionRequest.Create;
import gift.product.ProductRepository;
import gift.product.ProductService;
import gift.product.model.Product;
import gift.product.model.ProductRequest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    OptionRepository optionRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository, categoryRepository,
            optionRepository);
    }

    @Test
    void getAllProductsTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Direction.ASC, "id"));
        given(productRepository.findAll((Pageable) any())).willReturn(Page.empty());

        productService.getAllProducts(pageable);

        then(productRepository).should().findAll(pageable);
    }

    @Test
    void getProductTest() {
        Category category = new Category("test", "##test", "test.jpg", "test");
        given(productRepository.findById(any())).willReturn(
            Optional.of(new Product(1L, "test", 1000, "test.jpg", category)));

        productService.getProductById(1L);

        then(productRepository).should().findById(any());
    }

    @Test
    void insertProductTest() {
        Category category = new Category("test", "##test", "test.jpg", "test");
        Product product = new Product(1L, "test", 1000, "test.jpg", category);
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));
        given(productRepository.save(any())).willReturn(product);
        given(optionRepository.save(any())).willReturn(
            new Option("option", 1, product)
        );

        productService.insertProduct(new ProductRequest.Create("test", 1000, "test.jpg", 1L,
            List.of(new Create("option", 1))));

        then(productRepository).should().save(any());
        then(optionRepository).should().save(any());
    }

    @Test
    void updateProductTest() {
        Category category = new Category("test", "##test", "test.jpg", "test");
        Product product = new Product(1L, "test", 1000, "test.jpg", null);
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        productService.updateProductById(1L,
            new ProductRequest.Update("test1", 1400, "test1.jpg", 1L));

        assertAll(
            () -> assertThat(product.getName()).isEqualTo("test1"),
            () -> assertThat(product.getPrice()).isEqualTo(1400),
            () -> assertThat(product.getImageUrl()).isEqualTo("test1.jpg"),
            () -> assertThat(product.getCategory()).isEqualTo(category)
        );
    }

    @Test
    void deleteProductTest() {
        productService.deleteProductById(1L);

        then(productRepository).should().deleteById(any());
    }
}
