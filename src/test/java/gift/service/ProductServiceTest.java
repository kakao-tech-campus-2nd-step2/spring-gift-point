package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import gift.domain.category.entity.Category;
import gift.domain.category.repository.CategoryRepository;
import gift.domain.option.dto.OptionRequest;
import gift.domain.option.dto.OptionResponse;
import gift.domain.option.repository.OptionRepository;
import gift.domain.option.service.OptionService;
import gift.domain.product.dto.ProductCreateResponse;
import gift.domain.product.dto.ProductRequest;
import gift.domain.product.dto.ProductResponse;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductRepository;
import gift.domain.product.service.ProductService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;
    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    OptionRepository optionRepository;

    @Mock
    OptionService optionService;

    @Test
    @DisplayName("Id로 Product 조회 테스트")
    void getProductTest() {
        // given
        Long requestId = 1L;
        Product savedProduct = createProduct();

        doReturn(Optional.of(savedProduct)).when(productRepository).findById(any());

        ProductResponse expected = entityToDto(savedProduct);

        // when
        ProductResponse actual = productService.getProduct(requestId);

        // then
        assertAll(
            () -> assertThat(actual.name()).isEqualTo(expected.name()),
            () -> assertThat(actual.price()).isEqualTo(expected.price()),
            () -> assertThat(actual.imageUrl()).isEqualTo(expected.imageUrl()),
            () -> assertThat(actual.categoryId()).isEqualTo(expected.categoryId())
        );
    }

    @Test
    @DisplayName("모든 Product 조회 테스트")
    void getAllProductsTest() {
        // given
        Product product1 = createProduct();
        Product product2 = createProduct(2L,
            new Category(2L, "test", "color", "image", "description"));

        List<Product> productList = Arrays.asList(product1, product2);
        Pageable pageable = PageRequest.of(0, 10);

        Page<Product> pageList = new PageImpl<>(productList, pageable, productList.size());
        Page<ProductResponse> expected = pageList.map(this::entityToDto);

        doReturn(pageList).when(productRepository).findAll(pageable);

        // when
        Page<ProductResponse> actual = productService.getAllProducts(pageable.getPageNumber(),
            pageable.getPageSize());

        // then
        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> IntStream.range(0, actual.getContent().size()).forEach(i -> {
                assertThat(actual.getContent().get(i).name())
                    .isEqualTo(expected.getContent().get(i).name());
                assertThat(actual.getContent().get(i).price())
                    .isEqualTo(expected.getContent().get(i).price());
                assertThat(actual.getContent().get(i).imageUrl())
                    .isEqualTo(expected.getContent().get(i).imageUrl());
                assertThat(actual.getContent().get(i).categoryId())
                    .isEqualTo(expected.getContent().get(i).categoryId());
            })
        );

    }

    @Test
    @DisplayName("product 저장 테스트")
    void createProductTest() {
        // given
        ProductRequest productRequest = createProductRequest();
        Product newProduct = createProduct();

        OptionResponse optionResponse = new OptionResponse(1L, "name", 10);

        doReturn(Optional.of(newProduct.getCategory())).when(categoryRepository).findById(any());
        doReturn(newProduct).when(productRepository).save(any());
        doReturn(optionResponse).when(optionService).addOptionToProduct(any(), any());

        ProductCreateResponse expected = new ProductCreateResponse(entityToDto(newProduct),
            optionResponse);

        // when
        ProductCreateResponse actual = productService.createProduct(productRequest);

        // then
        assertAll(
            () -> assertThat(actual.name()).isEqualTo(expected.name()),
            () -> assertThat(actual.price()).isEqualTo(expected.price()),
            () -> assertThat(actual.imageUrl()).isEqualTo(expected.imageUrl()),
            () -> assertThat(actual.categoryId()).isEqualTo(expected.categoryId()),
            () -> assertThat(actual.optionResponse().name()).isEqualTo(
                expected.optionResponse().name()),
            () -> assertThat(actual.optionResponse().quantity()).isEqualTo(
                expected.optionResponse().quantity())
        );
    }

    @Test
    @DisplayName("product 업데이트 테스트")
    void updateProductTest() {
        // given
        Long requestId = 1L;
        ProductRequest productRequest = createProductRequest("test");

        Product updatedProduct = createProduct();
        Product spyProduct = spy(updatedProduct);

        ProductResponse expected = entityToDto(updatedProduct);

        doReturn(Optional.of(updatedProduct.getCategory())).when(categoryRepository)
            .findById(any());
        doReturn(Optional.of(spyProduct)).when(productRepository).findById(any());
        doNothing().when(spyProduct).updateAll(productRequest.getName(), productRequest.getPrice(),
            productRequest.getImageUrl(), updatedProduct.getCategory());

        // when
        ProductResponse actual = productService.updateProduct(requestId, productRequest);

        // then
        assertAll(
            () -> assertThat(actual.name()).isEqualTo(expected.name()),
            () -> assertThat(actual.price()).isEqualTo(expected.price()),
            () -> assertThat(actual.imageUrl()).isEqualTo(expected.imageUrl()),
            () -> assertThat(actual.categoryId()).isEqualTo(expected.categoryId())
        );
    }

    @Test
    @DisplayName("product 삭제 테스트")
    void deleteProductTest0() {
        // given
        Long id = 1L;
        Product savedProduct = createProduct();

        doReturn(Optional.of(savedProduct)).when(productRepository).findById(any(Long.class));
        // when
        productService.deleteProduct(id);

        // then
        verify(productRepository, times(1)).delete(any(Product.class));
    }

    private ProductResponse entityToDto(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategory().getId());
    }

    private ProductRequest createProductRequest() {
        return createProductRequest("test");
    }

    private ProductRequest createProductRequest(String name) {
        OptionRequest optionRequest = new OptionRequest("test", 100);
        return new ProductRequest(name, 1000, "test.jpg", 1L, optionRequest);
    }

    private Product createProduct() {
        return createProduct(1L, new Category(1L, "test", "color", "image", "description"));
    }

    private Product createProduct(Long id, Category category) {
        return new Product(id, "test", 1000, "test.jpg", category);
    }
}