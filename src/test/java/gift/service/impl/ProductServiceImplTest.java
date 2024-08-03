package gift.service.impl;

import gift.exception.ForbiddenWordException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product testProduct;
    private Category testCategory;
    private Option option1;
    private Option option2;

    public ProductServiceImplTest() {
    }

    @BeforeEach
    public void setUp() {
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");
        testCategory.setColor("#FFFFFF");
        testCategory.setImageUrl("http://example.com/image.jpg");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Product");
        testProduct.setPrice(1000);
        testProduct.setImageUrl("http://example.com/product_image.jpg");
        testProduct.setCategory(testCategory);

        option1 = new Option();
        option1.setName("Option 1");
        option1.setQuantity(10);
        option1.setProduct(testProduct);

        option2 = new Option();
        option2.setName("Option 2");
        option2.setQuantity(20);
        option2.setProduct(testProduct);

        testProduct.setOptions(Arrays.asList(option1, option2));
    }

    @Test
    public void testCreateProduct_Success() {
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(optionRepository.save(any(Option.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean success = productService.createProduct(testProduct);

        assertThat(success).isTrue();
        verify(productRepository, times(1)).save(any(Product.class));
        verify(optionRepository, times(2)).save(any(Option.class));
    }

    @Test
    public void testCreateProduct_ForbiddenWord() {
        testProduct.setName("카카오 상품");

        assertThatThrownBy(() -> productService.createProduct(testProduct))
            .isInstanceOf(ForbiddenWordException.class)
            .hasMessage("상품 이름에 '카카오'가 포함된 경우 담당 MD와 협의가 필요합니다.");
    }

    @Test
    public void testUpdateProduct_Success() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);
        when(optionRepository.save(any(Option.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setPrice(1200);
        updatedProduct.setImageUrl("http://example.com/updated_product_image.jpg");
        updatedProduct.setCategory(testCategory);
        updatedProduct.setOptions(Arrays.asList(option1, option2));

        boolean success = productService.updateProduct(testProduct.getId(), updatedProduct);

        assertThat(success).isTrue();
        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(optionRepository, times(2)).save(any(Option.class));
        verify(optionRepository, times(1)).deleteAll(anyIterable());
    }

    @Test
    public void testUpdateProduct_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");

        boolean success = productService.updateProduct(1L, updatedProduct);

        assertThat(success).isFalse();
        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, never()).save(any(Product.class));
        verify(optionRepository, never()).deleteAll(anyIterable());
    }
}
