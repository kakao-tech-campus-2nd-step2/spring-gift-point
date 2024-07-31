package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.category.model.Category;
import gift.category.repository.CategoryRepository;
import gift.exception.NonIntegerPriceException;
import gift.exception.OptionNotFoundException;
import gift.exception.ResourceNotFoundException;
import gift.option.model.Option;
import gift.option.repository.OptionRepository;
import gift.product.dto.ProductRequest;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequest productRequest;
    private Option option;

    @BeforeEach
    void setUp() {
        Category category = new Category("상품권");
        category.setId(1L);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        var byId = categoryRepository.findById(1L);
        category = byId.orElseThrow(() -> new ResourceNotFoundException("없는 카테고리"));
        product = new Product(1L, "productName", 10000, "image.jpg", category);
        option = new Option("option", 1, product);
        productRequest = new ProductRequest(product.getName(), product.getPrice(), product.getImageUrl(), category.getId(), 1L);
    }


    @Test
    @DisplayName("상품 저장 후 전체 상품 조회")
    public void saveAndGetAllProductsTest() throws NonIntegerPriceException, OptionNotFoundException {
        // given
        var productList = Collections.singletonList(product);
        given(productRepository.findAll()).willReturn(productList);

        // when
        var allProducts = productService.getAllProducts();

        // then
        assertThat(allProducts).isEqualTo(productList);
    }

    @Test
    @DisplayName("상품 ID 탐색")
    public void saveAndGetProductByIDTest() throws Exception {
        // given
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // when
        Product foundProduct = productService.getProductById(product.getId());

        // then
        assertThat(foundProduct).isEqualTo(product);

        // verify
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    @DisplayName("상품 생성 테스트")
    public void createProductAndSaveTest() throws NonIntegerPriceException, OptionNotFoundException {
        // given
        when(optionRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(option));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // when
        var savedProduct = productService.createProduct(productRequest);

        // then
        assertThat(savedProduct).isEqualTo(product);
    }

    @Test
    @DisplayName("상품 업데이트 테스트")
    public void updateProductTest() {
        // given
        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        var newProduct = new Product("newName", 20000, "newimage.jpg");
        newProduct.setId(1L);
        ProductRequest productRequest = new ProductRequest(newProduct.getName(),
            newProduct.getPrice(), newProduct.getImageUrl(),
            1L);

        // when
        var updatedProduct = productService.updateProduct(1L,productRequest);

        // then
        assertThat(updatedProduct.getId()).isEqualTo(product.getId());

    }

    @Test
    @DisplayName("상품 삭제 테스트")
    public void deleteProductTest() {
        // given
        doNothing().when(productRepository).deleteById(product.getId());

        // when
        var isDeleted = productService.deleteProduct(product.getId());

        // then
        assertThat(isDeleted).isTrue();

        // verify
        verify(productRepository, times(1)).deleteById(product.getId());
    }
}