package gift.service;

import static gift.util.CategoryFixture.createCategory;
import static gift.util.OptionFixture.createOption;
import static gift.util.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.OptionDTO;
import gift.dto.product.ProductDto;
import gift.repository.ProductRepository;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private CategoryService categoryService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OptionService optionService;

    @InjectMocks
    private ProductService productService;

    private Category category;

    @BeforeEach
    void setup() {
        category = createCategory(1L, "test");
    }

    @DisplayName("상품 추가")
    @Test
    void addProduct() {
        // given
        Product product = createProduct(1L, "아이스 아메리카노", category);
        OptionDTO optionDTO = createOption(1L, "test", 1, product).toDTO();
        given(categoryService.getCategory(anyLong())).willReturn(category.toDto());
        given(productRepository.save(any(Product.class))).willReturn(product);
        given(optionService.addOption(anyLong(), any(OptionDTO.class))).willReturn(optionDTO);

        // when
        var actual = productService.addProduct(product.toDto(), Arrays.asList(optionDTO));

        // then
        assertThat(actual.productDto()).isEqualTo(product.toDto());
        assertThat(actual.optionDTOs()).hasSize(1);
    }

    @DisplayName("id로 상품 찾기")
    @Test
    void getProduct() {
        // given
        long id = 1L;
        Product product = createProduct(id, "아이스 아메리카노", category);
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        ProductDto actual = productService.getProduct(id);

        // then
        assertThat(actual).isEqualTo(product.toDto());
    }

    @DisplayName("상품 수정")
    @Test
    void updateProduct() {
        // given
        long id = 1L;
        Product product = createProduct(id, "아이스 아메리카노", category);
        Product updatedProduct = createProduct(id, "핫 아메리카노", category);
        given(categoryService.getCategory(anyLong())).willReturn(category.toDto());
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(productRepository.save(any(Product.class))).willReturn(updatedProduct);

        // when
        ProductDto actual = productService.updateProduct(id, updatedProduct.toDto());

        // then
        assertThat(actual).isEqualTo(updatedProduct.toDto());
    }

    @DisplayName("상품 삭제")
    @Test
    void deleteProduct() {
        // given
        long id = 1L;
        Product product = createProduct(id, "아이스 아메리카노", category);
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        productService.deleteProduct(id);

        // then
        then(productRepository).should().delete(any(Product.class));
    }
}
