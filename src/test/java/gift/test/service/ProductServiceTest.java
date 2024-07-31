package gift.test.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;

import gift.dto.CategoryUpdateRequest;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.dto.ProductUpdateRequest;
import gift.entity.Category;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.service.ProductService;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ProductService productService;

    private Category category;
    private Product product;
    private ProductRequest productRequest;
    private ProductUpdateRequest productUpdateRequest;
    private CategoryUpdateRequest categoryUpdateRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
        product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", category);
        product.setId(1L);

        productRequest = new ProductRequest("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", "교환권");
        productUpdateRequest = new ProductUpdateRequest("아이스 아메리카노 T", 5000, "https://example.com/image.jpg");
        categoryUpdateRequest = new CategoryUpdateRequest("상품권");
    }

    @Test
    public void testGetProducts() {
    	Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(Collections.singletonList(product), pageable, 1);

        when(productRepository.findAll(pageable)).thenReturn(page);

        Page<ProductResponse> response = productService.getProducts(pageable);

        verify(productRepository).findAll(pageable);
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getName()).isEqualTo(product.getName());
    }

    @Test
    public void testCreateProduct() {
        Category category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        productService.createProduct(productRequest, bindingResult);
        
        verify(categoryRepository).findByName(anyString());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        productService.updateProduct(1L, productUpdateRequest, bindingResult);
        
        verify(productRepository).findById(anyLong());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testUpdateProductCategory() {
        Category newCategory = new Category("상품권", "#6c95d1", "https://example.com/new_image.jpg", "");
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(newCategory));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        productService.updateProductCategory(1L, categoryUpdateRequest, bindingResult);
        
        verify(productRepository).findById(anyLong());
        verify(categoryRepository).findByName(anyString());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        when(productRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(productRepository).deleteById(anyLong());
        productService.deleteProduct(1L);
        
        verify(productRepository).existsById(anyLong());
        verify(productRepository).deleteById(anyLong());
    }
}
