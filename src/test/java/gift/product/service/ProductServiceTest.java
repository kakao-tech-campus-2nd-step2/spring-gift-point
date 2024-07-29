package gift.product.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.product.dto.ProductDTO;
import gift.product.model.Category;
import gift.product.model.Option;
import gift.product.model.Product;
import gift.product.repository.CategoryRepository;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import gift.product.validation.ProductValidation;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private ProductValidation productValidation;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private OptionRepository optionRepository;

    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category(1L,"교환권");
        product = new Product(1L, "originalProduct", 1000, "image", category);
    }

    @Test
    void testRegisterProduct() {
        // given
        ProductDTO productDTO = new ProductDTO("normalProduct", 1000, "image.url", category.getId());
        when(categoryRepository.findById(anyLong()))
            .thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class)))
            .thenReturn(product);

        // when
        productService.registerProduct(productDTO);

        // then
        verify(productValidation).registerValidation(productDTO);
        verify(productRepository).save(any(Product.class));
        verify(optionRepository).save(any(Option.class));
    }

    @Test
    void testUpdateProduct() {
        // given
        ProductDTO productDTO = new ProductDTO("product", 2000, "image", 1L);
        when(productRepository.findById(anyLong()))
            .thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyLong()))
            .thenReturn(Optional.of(category));

        // when
        productService.updateProduct(product.getId(), productDTO);

        // then
        verify(productValidation).updateValidation(1L, productDTO);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        // given
        when(productRepository.findById(anyLong()))
            .thenReturn(Optional.of(product));

        // when
        productService.deleteProduct(1L);

        // then
        verify(productValidation).deleteValidation(1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void testGetAllProducts() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product), pageable, 1);
        when(productRepository.findAll(pageable))
            .thenReturn(productPage);

        // when
        Page<Product> result = productService.getAllProducts(pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("originalProduct", result.getContent().getFirst().getName());
    }


}