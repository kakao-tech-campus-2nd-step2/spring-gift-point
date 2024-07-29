package gift.application;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.product.application.ProductService;
import gift.product.dao.CategoryRepository;
import gift.product.dao.ProductRepository;
import gift.product.dto.OptionRequest;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;
import gift.product.entity.Category;
import gift.product.entity.Product;
import gift.product.util.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import testFixtures.CategoryFixture;
import testFixtures.ProductFixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private Category category;

    private OptionRequest optionRequest;

    @BeforeEach
    void setUp() {
        category = CategoryFixture.createCategory("상품권");
        optionRequest = new OptionRequest("옵션", 10);
    }

    @Test
    @DisplayName("상품 전체 조회 서비스 테스트")
    void getPagedProducts() {
        List<Product> productList = new ArrayList<>();
        Product product1 = ProductFixture.createProduct("product1", category);
        Product product2 = ProductFixture.createProduct("product2", category);
        productList.add(product1);
        productList.add(product2);
        Page<Product> productPage = new PageImpl<>(productList);
        given(productRepository.findAll(productPage.getPageable()))
                .willReturn(productPage);

        Page<ProductResponse> responsePage = productService.getPagedProducts(productPage.getPageable());

        assertThat(responsePage.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("상품 상세 조회 서비스 테스트")
    void getProductById() {
        Product product = ProductFixture.createProduct("product", category);
        given(productRepository.findById(any()))
                .willReturn(Optional.of(product));

        ProductResponse resultProduct = productService.getProductByIdOrThrow(1L);

        assertThat(resultProduct.name()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("상품 서비스 상세 조회 실패 테스트")
    void getProductByIdFailed() {
        Long productId = 1L;
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductByIdOrThrow(productId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND
                                     .getMessage());
    }

    @Test
    @DisplayName("상품 추가 서비스 테스트")
    void createProduct() {
        ProductRequest request = new ProductRequest(
                "product1",
                1000,
                "https://testshop.com",
                category.getName(),
                optionRequest);
        Product product = ProductMapper.toEntity(request, category);
        given(productRepository.save(any()))
                .willReturn(product);
        given(categoryRepository.findByName(any()))
                .willReturn(Optional.of(category));

        ProductResponse response = productService.createProduct(request);

        assertThat(response.name()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("단일 상품 삭제 서비스 테스트")
    void deleteProductById() {
        Long productId = 1L;

        productService.deleteProductById(productId);

        verify(productRepository).deleteById(productId);
    }

    @Test
    @DisplayName("상품 전체 삭제 서비스 테스트")
    void deleteAllProducts() {
        productService.deleteAllProducts();

        verify(productRepository).deleteAll();
    }

    @Test
    @DisplayName("상품 수정 서비스 테스트")
    void updateProduct() {
        Product product = ProductFixture.createProduct("product", category);
        ProductRequest request = new ProductRequest(
                "product2",
                product.getPrice(),
                product.getImageUrl(),
                category.getName(),
                optionRequest);
        given(productRepository.findById(any()))
                .willReturn(Optional.of(product));
        given(categoryRepository.findByName(any()))
                .willReturn(Optional.of(category));

        productService.updateProduct(product.getId(), request);

        assertThat(product.getName()).isEqualTo(request.name());
    }

    @Test
    @DisplayName("상품 서비스 수정 실패 테스트")
    void updateProductFailed() {
        Long productId = 1L;
        ProductRequest request = new ProductRequest(
                "product",
                3000,
                "https://testshop.io",
                category.getName(),
                optionRequest);
        given(productRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> productService.updateProduct(productId, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND
                                     .getMessage());
    }

}