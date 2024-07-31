package gift.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.any;

import gift.exception.CustomException;
import gift.product.category.entity.Category;
import gift.product.category.repository.CategoryJpaRepository;
import gift.product.dto.request.CreateProductRequest;
import gift.product.dto.request.NewOption;
import gift.product.dto.request.UpdateProductRequest;
import gift.product.dto.response.ProductResponse;
import gift.product.entity.Product;
import gift.product.option.dto.response.OptionResponse;
import gift.product.option.service.OptionService;
import gift.product.repository.ProductJpaRepository;
import gift.product.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductJpaRepository productRepository;

    @Mock
    private CategoryJpaRepository categoryRepository;

    @Mock
    private OptionService optionService;

    @Test
    @DisplayName("getAllProducts empty test")
    @Transactional
    void getAllProductsEmptyTest() {
        // given
        Pageable pageable = Pageable.unpaged();
        given(productRepository.findAll(pageable)).willReturn(Page.empty());

        // when
        Page<ProductResponse> products = productService.getAllProducts(pageable);

        // then
        assertThat(products).isEmpty();
    }

    @Test
    @DisplayName("getAllProducts test")
    @Transactional
    void getAllProductsTest() {
        // given
        Pageable pageable = Pageable.unpaged();
        Category category = new Category("Category A");
        List<Product> productList = List.of(
            new Product("Product A", 1000, "http://example.com/images/product_a.jpg", category),
            new Product("Product B", 2000, "http://example.com/images/product_b.jpg", category),
            new Product("Product C", 3000, "http://example.com/images/product_c.jpg", category),
            new Product("Product D", 4000, "http://example.com/images/product_d.jpg", category),
            new Product("Product E", 5000, "http://example.com/images/product_e.jpg", category)
        );
        Page<Product> productPage = new PageImpl<>(productList);
        given(productRepository.findAll(pageable)).willReturn(productPage);

        // when
        List<ProductResponse> products = productService.getAllProducts(pageable).toList();

        // then
        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(5);
        then(productRepository).should().findAll(pageable);
    }

    @Test
    @DisplayName("getProductById exception test")
    @Transactional
    void getProductByIdExceptionTest() {
        // given
        given(productRepository.findById(7L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.getProductById(7L)).isInstanceOf(
            CustomException.class);
        then(productRepository).should().findById(7L);
    }

    @Test
    @DisplayName("getProductById test")
    @Transactional
    void getProductByIdTest() {
        // given
        Product expected = new Product("Product B", 2000, "http://example.com/images/product_b.jpg",
            null);
        given(productRepository.findById(2L)).willReturn(Optional.of(expected));

        // when
        Product actual = productRepository.findById(2L).get();

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl());
        then(productRepository).should().findById(2L);
    }

    @Test
    @DisplayName("Add product test")
    void createProductTest() {
        // given
        NewOption option = new NewOption("option 1", 100);
        CreateProductRequest request = new CreateProductRequest("Product A", 1000,
            "http://example.com/images/product_a.jpg", 1L, List.of(option));
        Category category = new Category("Category A", "#123456", "description", "image");
        Product savedProduct = new Product("Product A", 1000,
            "http://example.com/images/product_a.jpg", category);
        given(productRepository.save(any(Product.class))).willReturn(savedProduct);
        given(categoryRepository.findById(any(Long.class))).willReturn(Optional.of(category));
        given(optionService.createOption(any(), any())).willReturn(
            new OptionResponse(1L, "option 1", 100));

        // when
        productService.createProduct(request);

        // then
        then(productRepository).should().save(any(Product.class));
        then(categoryRepository).should().findById(any(Long.class));
    }

    @Test
    @DisplayName("updateProduct test")
    @Transactional
    void updateProductTest() {
        // given
        Category category1 = new Category("Category A", "#123456", "image", "");
        Product product = new Product("Product A", 1000, "http://example.com/images/product_a.jpg",
            null);
        UpdateProductRequest request = new UpdateProductRequest("product3", 30000, null, 1L);
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(categoryRepository.findById(any(Long.class))).willReturn(Optional.of(category1));

        // when
        productService.updateProduct(1L, request);

        // then
        then(productRepository).should().findById(1L);
    }

    @Test
    @DisplayName("deleteProduct test")
    @Transactional
    void deleteProductTest() {
        // given
        given(productRepository.existsById(1L)).willReturn(true);
        willDoNothing().given(productRepository).deleteById(1L);

        // when
        productService.deleteProduct(1L);

        // then
        then(productRepository).should().existsById(1L);
        then(productRepository).should().deleteById(1L);
    }

    @Test
    @DisplayName("deleteProduct exception test")
    @Transactional
    void deleteProductExceptionTest() {
        // given
        given(productRepository.existsById(9L)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> productService.deleteProduct(9L)).isInstanceOf(
            CustomException.class);
        then(productRepository).should().existsById(9L);
    }
}
