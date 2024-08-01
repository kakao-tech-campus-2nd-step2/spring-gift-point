package gift.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.product.domain.Category;
import gift.product.domain.Product;
import gift.product.exception.product.ProductNotFoundException;
import gift.product.persistence.CategoryRepository;
import gift.product.persistence.ProductRepository;
import gift.product.service.command.ProductCommand;
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

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("ProductService 생성 테스트[성공]")
    void saveProductTest() {
        //given
        ProductCommand productCommand = new ProductCommand("테스트 상품", 1000, "http://test.com", 1L);

        Category category = new Category(1L, "카테고리", "카테고리 설명", "카테고리 이미지", "카테고리 썸네일 이미지");
        Product savedProduct = new Product(1L, "테스트 상품", 1000, "http://test.com", category);

        given(categoryRepository.findById(any())).willReturn(Optional.of(category));
        given(productRepository.save(any(Product.class))).willReturn(savedProduct);

        //when
        Long savedProductId = productService.saveProduct(productCommand);

        //then
        verify(productRepository).save(any(Product.class));

        assertThat(savedProductId).isEqualTo(1L);
    }

    @Test
    @DisplayName("ProductService 수정 테스트[성공]")
    void modifyProductTest() {
        //given
        Long id = 1L;
        ProductCommand productCommand = new ProductCommand("수정된 상품", 2000, "http://test.com", 1L);
        Category existCategory = new Category(1L, "카테고리", "카테고리 설명", "카테고리 이미지", "카테고리 썸네일 이미지");
        Product product = new Product(1L, "테스트 상품", 1000, "http://test.com", existCategory);
        Category newCategory = new Category(2L, "새 카테고리", "새 카테고리 설명", "새 카테고리 이미지", "새 카테고리 썸네일 이미지");

        given(categoryRepository.findById(any())).willReturn(Optional.of(newCategory));
        given(productRepository.findById(id)).willReturn(Optional.of(product));

        //when
        productService.modifyProduct(id, productCommand);

        //then
        verify(productRepository).findById(id);
        assertThat(product.getName()).isEqualTo("수정된 상품");
        assertThat(product.getPrice()).isEqualTo(2000);
        assertThat(product.getImgUrl()).isEqualTo("http://test.com");
        assertThat(product.getCategory()).isEqualTo(newCategory);
    }

    @Test
    @DisplayName("ProductService 삭제 테스트[성공]")
    void deleteProductTest() {
        //given
        final Long id = 1L;
        Category category = new Category(1L, "카테고리", "카테고리 설명", "카테고리 이미지", "카테고리 썸네일 이미지");
        Product product = new Product(id, "테스트 상품", 1000, "http://test.com", category);

        given(productRepository.findById(id)).willReturn(Optional.of(product));

        //when
        productService.deleteProduct(id);

        //then
        verify(productRepository).findById(id);
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("ProductService 조회 테스트[성공]")
    void getProductDetailsTest() {
        //given
        final Long id = 1L;
        Category category = new Category(1L, "카테고리", "카테고리 설명", "카테고리 이미지", "카테고리 썸네일 이미지");
        Product product = new Product(id, "테스트 상품", 1000, "http://test.com", category);

        given(productRepository.findByIdWithCategory(id)).willReturn(Optional.of(product));

        //when
        var productInfo = productService.getProductDetails(id);

        //then
        verify(productRepository).findByIdWithCategory(id);
        assertThat(productInfo.id()).isEqualTo(1L);
        assertThat(productInfo.name()).isEqualTo("테스트 상품");
        assertThat(productInfo.price()).isEqualTo(1000);
        assertThat(productInfo.imgUrl()).isEqualTo("http://test.com");
        assertThat(productInfo.category().id()).isEqualTo(1L);
        assertThat(productInfo.category().name()).isEqualTo("카테고리");
        assertThat(productInfo.category().imgUrl()).isEqualTo("카테고리 이미지");
        assertThat(productInfo.category().description()).isEqualTo("카테고리 썸네일 이미지");

    }

    @Test
    @DisplayName("ProductService 조회 테스트[실패]")
    void getProductDetailsFailTest() {
        //given
        final Long id = 1L;

        given(productRepository.findByIdWithCategory(id)).willReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> productService.getProductDetails(id))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("ProductService 목록 조회 테스트")
    void getProductsTest() {
        //given
        List<Category> categories = List.of(
                new Category(1L, "카테고리1", "카테고리 설명1", "http://test.com", "http://test.com"),
                new Category(2L, "카테고리2", "카테고리 설명2", "http://test.com", "http://test.com")
        );
        Page<Product> productPage = new PageImpl<>(List.of(
                new Product(1L, "테스트 상품1", 1000, "http://test.com", categories.get(0)),
                new Product(2L, "테스트 상품2", 2000, "http://test.com", categories.get(1))
        ));
        Pageable pageable = Pageable.ofSize(10).first();

        given(productRepository.findAllWithCategory(pageable)).willReturn(productPage);

        //when
        var products = productService.getProducts(pageable);

        //then
        verify(productRepository).findAllWithCategory(pageable);
    }

    @Test
    @DisplayName("ProductService 수정 테스트[실패]")
    void modifyProductFailTest() {
        //given
        final Long id = 1L;
        ProductCommand productCommand = new ProductCommand("수정된 상품", 2000, "http://test.com", 1L);

        //when
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> productService.modifyProduct(id, productCommand))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    @DisplayName("ProductService 삭제 테스트[실패]")
    void deleteProductFailTest() {
        //given
        final Long id = 1L;

        //when
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> productService.deleteProduct(id))
                .isInstanceOf(ProductNotFoundException.class);
    }
}