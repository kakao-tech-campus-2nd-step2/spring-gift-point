package gift.test.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import gift.controller.ProductController;
import gift.dto.CategoryUpdateRequest;
import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.dto.ProductUpdateRequest;
import gift.entity.Category;
import gift.entity.Product;
import gift.service.ProductService;

public class ProductTest {
	
	@Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Mock
    private BindingResult bindingResult;

    private Category category;
    private Product product;
    private ProductResponse productResponse;
    private ProductRequest productRequest;
    private ProductUpdateRequest productUpdateRequest;
    private CategoryUpdateRequest categoryUpdateRequest;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
        product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", category);
        product.setId(1L);
        
        productResponse = new ProductResponse(1L, "아이스 아메리카노 T", 4500, "https://example.com/image.jpg", "교환권");
        
        productRequest = new ProductRequest("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", "교환권");
        productUpdateRequest = new ProductUpdateRequest("아이스 아메리카노 T", 4500, "https://example.com/image.jpg");
        categoryUpdateRequest = new CategoryUpdateRequest("교환권");
    }

    @Test
    public void testGetProducts() {
    	Pageable pageable = PageRequest.of(0, 10);
    	Page<ProductResponse> productPage = new PageImpl<>(Collections.singletonList(productResponse), pageable, 1);
    	
        when(productService.getProducts(pageable)).thenReturn(productPage);

        ResponseEntity<Page<ProductResponse>> response = productController.getProducts(pageable);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(productPage);
    }

    @Test
    public void testGetProduct() {
        when(productService.getProduct(1L)).thenReturn(productResponse);

        ResponseEntity<ProductResponse> response = productController.getProduct(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(productResponse);
    }

    @Test
    public void testAddProduct() {
    	doNothing().when(productService).createProduct(any(ProductRequest.class), any(BindingResult.class));

        ResponseEntity<Void> response = productController.addProduct(productRequest, bindingResult);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
    }

    @Test
    public void testUpdateProduct() {
        doNothing().when(productService).updateProduct(eq(1L), any(ProductUpdateRequest.class), any(BindingResult.class));

        ResponseEntity<Void> response = productController.updateProduct(1L, productUpdateRequest, bindingResult);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
    
    @Test
    public void testUpdateProductCategory() {
    	doNothing().when(productService).updateProductCategory(eq(1L), any(CategoryUpdateRequest.class), any(BindingResult.class));
    	
    	ResponseEntity<Void> response = productController.updateProductCategory(1L, categoryUpdateRequest, bindingResult);
    	
    	assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }
}
