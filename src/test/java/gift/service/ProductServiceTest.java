package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import gift.converter.StringToUrlConverter;
import gift.domain.Category;
import gift.domain.Category.Builder;
import gift.domain.Product;
import gift.domain.ProductOption;
import gift.domain.vo.Color;
import gift.repository.CategoryRepository;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishProductRepository;
import gift.web.dto.request.product.CreateProductRequest;
import gift.web.dto.request.product.UpdateProductRequest;
import gift.web.dto.request.productoption.CreateProductOptionRequest;
import gift.web.dto.response.product.CreateProductResponse;
import gift.web.dto.response.product.ReadAllProductsResponse;
import gift.web.dto.response.product.ReadProductResponse;
import gift.web.dto.response.product.UpdateProductResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;

    private ProductOptionService productOptionService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private WishProductRepository wishProductRepository;

    @Mock
    private ProductOptionRepository productOptionRepository;

    @BeforeEach
    void setUp() {
        productOptionService = new ProductOptionService(productOptionRepository, productRepository);
        productService = new ProductService(productRepository, categoryRepository, wishProductRepository, productOptionService);
    }

    @Nested
    @DisplayName("createProduct 메서드는")
    class createProduct {

        @Test
        @DisplayName("상품 생성 요청이 정상적일 때, 상품을 성공적으로 생성합니다.")
        void when_valid_request() {
            //given
            CreateProductOptionRequest optionRequest1 = new CreateProductOptionRequest("옵션01", 100);
            CreateProductOptionRequest optionRequest2 = new CreateProductOptionRequest("옵션02", 100);
            CreateProductRequest request = new CreateProductRequest("상품01", 10000,
                "https://www.google.com", 1L, List.of(optionRequest1));

            Category category = new Builder()
                .id(1L)
                .name("카테고리01")
                .description("카테고리01 설명")
                .imageUrl(StringToUrlConverter.convert("https://www.google.com"))
                .color(Color.from("#FFFFFF"))
                .build();

            Product product = request.toEntity(category);
            given(categoryRepository.findById(any())).willReturn(Optional.of(category));
            given(productRepository.save(any())).willReturn(product);

            //when
            CreateProductResponse response = productService.createProduct(request);

            //then
            assertAll(
                () -> assertThat(response.getName()).isEqualTo(product.getName()),
                () -> assertThat(response.getPrice()).isEqualTo(product.getPrice()),
                () -> assertThat(response.getImageUrl()).isEqualTo(product.getImageUrl().toString()),

                () -> assertThat(response.getOptions().size()).isEqualTo(product.getProductOptions().size()),
                () -> assertThat(response.getOptions().get(0).getName()).isEqualTo(product.getProductOptions().get(0).getName()),
                () -> assertThat(response.getOptions().get(0).getStock()).isEqualTo(product.getProductOptions().get(0).getStock())
            );
        }

        @Test
        @DisplayName("상품 생성 시 옵션 이름에 중복이 존재하면 예외를 발생시킵니다.")
        void when_duplicate_option_name() {
            //given
            CreateProductOptionRequest optionRequest1 = new CreateProductOptionRequest("옵션01", 100);
            CreateProductOptionRequest optionRequest2 = new CreateProductOptionRequest("옵션01", 100);
            CreateProductRequest request = new CreateProductRequest("상품01", 10000,
                "https://www.google.com", 1L, List.of(optionRequest1, optionRequest2));

            Category category = new Builder()
                .id(1L)
                .name("카테고리01")
                .description("카테고리01 설명")
                .imageUrl(StringToUrlConverter.convert("https://www.google.com"))
                .color(Color.from("#FFFFFF"))
                .build();

            Product product = request.toEntity(category);
            given(categoryRepository.findById(any())).willReturn(Optional.of(category));
            given(productRepository.save(any())).willReturn(product);

            //when
            //then
            assertThatThrownBy(() -> productService.createProduct(request))
                .isInstanceOf(IllegalStateException.class);
        }

    }

    @Test
    @DisplayName("상품 조회 요청이 정상적일 때, 상품을 성공적으로 조회합니다.")
    void readProductById() {
        //given
        Product product = new Product.Builder()
            .id(1L)
            .name("상품01")
            .price(10000)
            .imageUrl(StringToUrlConverter.convert("https://www.google.com"))
            .category(new Builder().id(1L).name("카테고리01").description("카테고리01 설명").imageUrl(StringToUrlConverter.convert("https://www.google.com")).color(Color.from("#FFFFFF")).build())
            .productOptions(List.of(new ProductOption.Builder().id(1L).name("옵션01").stock(100).build()))
            .build();

        given(productRepository.findById(any())).willReturn(Optional.of(product));

        //when
        ReadProductResponse response = productService.readProductById(1L);

        //then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(product.getId()),
            () -> assertThat(response.getName()).isEqualTo(product.getName()),
            () -> assertThat(response.getPrice()).isEqualTo(product.getPrice()),
            () -> assertThat(response.getImageUrl()).isEqualTo(product.getImageUrl().toString()),
            () -> assertThat(response.getCategory().getId()).isEqualTo(product.getCategory().getId()),
            () -> assertThat(response.getCategory().getName()).isEqualTo(product.getCategory().getName())
        );
    }

    @Test
    @DisplayName("카테고리 ID로 상품 조회 요청이 정상적일 때, 해당 카테고리에 속한 상품들을 성공적으로 조회합니다.")
    void readProductsByCategoryId() {
        //given
        Category category = new Builder()
            .id(1L)
            .name("카테고리01")
            .description("카테고리01 설명")
            .imageUrl(StringToUrlConverter.convert("https://www.google.com"))
            .color(Color.from("#FFFFFF"))
            .build();

        Product product01 = new Product.Builder()
            .id(1L)
            .name("상품01")
            .price(10000)
            .imageUrl(StringToUrlConverter.convert("https://www.google.com"))
            .category(category)
            .productOptions(List.of(new ProductOption.Builder().id(1L).name("옵션01").stock(100).build()))
            .build();

        Product product02 = new Product.Builder()
            .id(2L)
            .name("상품02")
            .price(20000)
            .imageUrl(StringToUrlConverter.convert("https://www.google.com"))
            .category(category)
            .productOptions(List.of(new ProductOption.Builder().id(2L).name("옵션02").stock(200).build()))
            .build();

        given(productRepository.findByCategoryId(any(), any())).willReturn(List.of(product01, product02));

        //when
        ReadAllProductsResponse response = productService.readProductsByCategoryId(
            1L, null);

        //then
        assertAll(
            () -> assertThat(response.getProducts().size()).isEqualTo(2),

            () -> assertThat(response.getProducts().get(0).getId()).isEqualTo(product01.getId()),
            () -> assertThat(response.getProducts().get(0).getName()).isEqualTo(product01.getName()),
            () -> assertThat(response.getProducts().get(0).getPrice()).isEqualTo(product01.getPrice()),
            () -> assertThat(response.getProducts().get(0).getImageUrl()).isEqualTo(product01.getImageUrl().toString()),
            () -> assertThat(response.getProducts().get(0).getCategory().getId()).isEqualTo(product01.getCategory().getId()),
            () -> assertThat(response.getProducts().get(0).getCategory().getName()).isEqualTo(product01.getCategory().getName()),

            () -> assertThat(response.getProducts().get(1).getId()).isEqualTo(product02.getId()),
            () -> assertThat(response.getProducts().get(1).getName()).isEqualTo(product02.getName()),
            () -> assertThat(response.getProducts().get(1).getPrice()).isEqualTo(product02.getPrice()),
            () -> assertThat(response.getProducts().get(1).getImageUrl()).isEqualTo(product02.getImageUrl().toString()),
            () -> assertThat(response.getProducts().get(1).getCategory().getId()).isEqualTo(product02.getCategory().getId()),
            () -> assertThat(response.getProducts().get(1).getCategory().getName()).isEqualTo(product02.getCategory().getName())
        );
    }

    @Test
    @DisplayName("상품 수정 요청이 정상적일 때, 상품을 성공적으로 수정합니다.")
    void updateProduct() {
        //given
        Product product = new Product.Builder()
            .id(1L)
            .name("상품01")
            .price(10000)
            .imageUrl(StringToUrlConverter.convert("https://www.google.com"))
            .category(new Builder().id(1L).name("카테고리01").description("카테고리01 설명").imageUrl(StringToUrlConverter.convert("https://www.google.com")).color(Color.from("#FFFFFF")).build())
            .productOptions(List.of(new ProductOption.Builder().id(1L).name("옵션01").stock(100).build()))
            .build();

        given(productRepository.findById(any())).willReturn(Optional.of(product));

        UpdateProductRequest request = new UpdateProductRequest("상품02", 20000, "https://www.naver.com");

        //when
        UpdateProductResponse response = productService.updateProduct(1L, request);

        //then
        assertAll(
            () -> assertThat(response.getId()).isEqualTo(product.getId()),
            () -> assertThat(response.getName()).isEqualTo(request.getName()),
            () -> assertThat(response.getPrice()).isEqualTo(request.getPrice()),
            () -> assertThat(response.getImageUrl()).isEqualTo(request.getImageUrl())
        );
    }

    @Test
    @DisplayName("상품 삭제 요청이 정상적일 때, 상품을 성공적으로 삭제합니다.")
    void deleteProduct() {
        //given
        Product product = new Product.Builder()
            .id(1L)
            .name("상품01")
            .price(10000)
            .imageUrl(StringToUrlConverter.convert("https://www.google.com"))
            .category(new Builder().id(1L).name("카테고리01").description("카테고리01 설명").imageUrl(StringToUrlConverter.convert("https://www.google.com")).color(Color.from("#FFFFFF")).build())
            .productOptions(List.of(new ProductOption.Builder().id(1L).name("옵션01").stock(100).build()))
            .build();

        given(productRepository.findById(any())).willReturn(Optional.of(product));

        //when
        //then
        assertDoesNotThrow(() -> productService.deleteProduct(1L));
    }
}