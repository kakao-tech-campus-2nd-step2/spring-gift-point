package gift.service;

import gift.dto.InputProductDTO;
import gift.dto.UpdateProductDTO;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OptionService optionService;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("해당 id 갖는 product 반환")
    void getProductByIdTest() {
        // given
        given(productRepository.findById(any()))
                .willReturn(Optional.of(new Product(1L,"product", 1000, "image.url", null)));
        // when
        productService.getProductById(1L);
        // then
        then(productRepository).should().findById(1L);
    }

    @Test
    @DisplayName("product 저장")
    void saveProductTest() {
        // give
        Category category = new Category("교환권");
        Product savedProduct = new Product(1L, "product", 1000, "image.url", category);

        given(categoryRepository.findByName("교환권"))
                .willReturn(Optional.of(category));
        given(productRepository.save(any(Product.class)))
                .willReturn(savedProduct);
        given(productRepository.findByName("product"))
                .willReturn(Optional.of(savedProduct));
        // When
        InputProductDTO inputProductDTO = new InputProductDTO("product", 1000, "image.url", "교환권", "option1,option2", "100,200");
        productService.saveProduct(inputProductDTO);

        // Then
        then(productRepository).should().save(any(Product.class));
        then(optionService).should().addOptions("option1,option2", "100,200", 1L);
    }

    @Test
    @DisplayName("product 삭제")
    void deleteProductTest() {
        // given
        Long productId = 1L;
        // when
        productService.deleteProduct(productId);
        // then
        then(productRepository).should().deleteById(productId);
    }

    @Test
    @DisplayName("product 업데이트")
    void updateProductTest() {
        // given
        UpdateProductDTO updateProductDTO = new UpdateProductDTO("product", 1000, "image.url", "교환권");
        Category category = new Category("교환권");
        given(productRepository.findById(1L))
                .willReturn(Optional.of(new Product(1L, "Product", 1000, "image.url", category)));
        given(categoryRepository.findByName(any()))
                .willReturn(Optional.of(category));
        // when
        productService.updateProduct(1L, updateProductDTO);
        // then
        then(productRepository).should().findById(1L);
        then(productRepository).should().save(any(Product.class));
    }
}