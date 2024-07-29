package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.dto.ProductDto;
import gift.exception.NonIntegerPriceException;
import gift.exception.ResourceNotFoundException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
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
    private ProductDto productDto;
    private Option option;

    @BeforeEach
    void setUp() {
        Category category = new Category("상품권");
        category.setId(1L);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));
        var byId = categoryRepository.findById(1L);
        category = byId.orElseGet(() -> {throw new ResourceNotFoundException("없는 카테고리");});
        product = new Product(1L, "productName", 10000, "image.jpg", category);
        option = new Option("option", 1, product);
        productDto = new ProductDto(product.getName(), product.getPrice(), product.getImageUrl(), category.getId(), 1L);
    }


    @Test
    @DisplayName("상품 저장 후 전체 상품 조회")
    public void saveAndGetAllProductsTest() throws NonIntegerPriceException {
        // given
        given(productRepository.save(product)).willReturn(product);
        var productList = Collections.singletonList(product);
        given(productRepository.findAll()).willReturn(productList);
        given(optionRepository.findById(1L)).willReturn(Optional.ofNullable(option));

        // when
        Product savedProduct = productService.createProduct(productDto);  // 실제 저장 호출
        var allProducts = productService.getAllProducts();

        // then
        assertThat(savedProduct).isEqualTo(product);
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
    public void createProductAndSaveTest() throws NonIntegerPriceException {
        // given
        when(productRepository.save(product)).thenReturn(product);
        when(optionRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(option));

        // when
        var savedProduct = productService.createProduct(productDto);

        // then
        assertThat(savedProduct).isEqualTo(product);
    }

    @Test
    @DisplayName("상품 업데이트 테스트")
    public void updateProductTest() throws Exception {
        // given
        when(productRepository.save(product)).thenReturn(product);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        var newProduct = new Product("newName", 20000, "newimage.jpg");
        newProduct.setId(1L);

        // when
        var updatedProduct = productService.updateProduct(newProduct);

        // then
        assertThat(updatedProduct.getId()).isEqualTo(product.getId());

        // verify
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    public void deleteProductTest() throws Exception {
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