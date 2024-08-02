package gift.Controller;

import gift.Service.ProductService;
import gift.DTO.ProductDTO;
import gift.Entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

//    @Test
//    public void testGetAllProducts() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<ProductDTO> page = new PageImpl<>(Collections.emptyList());
//        when(productService.getProducts(pageable)).thenReturn(page);
//
//        Page<ProductDTO> result = productController.getAllProducts(pageable);
//
//        assertEquals(0, result.getTotalElements());
//        verify(productService, times(1)).getProducts(pageable);
//    }
//
//    @Test
//    public void testGetProductById() {
//        Long id = 1L;
//        ProductEntity product = createSampleProductEntity();
//        when(productService.findProductById(id)).thenReturn(Optional.of(product));
//
//        ResponseEntity<ProductEntity> response = productController.getProductById(id);
//
//        assertResponse(response, ResponseEntity.ok(product));
//        verify(productService, times(1)).findProductById(id);
//    }
//
//    @Test
//    public void testCreateProduct() {
//        ProductEntity product = createSampleProductEntity();
//        when(productService.saveProduct(product)).thenReturn(product);
//
//        ProductEntity result = productController.createProduct(product);
//
//        assertEquals(product, result);
//        verify(productService, times(1)).saveProduct(product);
//    }

    @Test
    public void testDeleteProduct() {
        Long id = 1L;
        doNothing().when(productService).deleteProduct(id);

        ResponseEntity<Void> response = productController.deleteProduct(id);

        assertResponse(response, ResponseEntity.noContent().build());
        verify(productService, times(1)).deleteProduct(id);
    }

    private ProductEntity createSampleProductEntity() {
        return new ProductEntity();
    }

    private <T> void assertResponse(ResponseEntity<T> actualResponse, ResponseEntity<T> expectedResponse) {
        assertEquals(expectedResponse, actualResponse);
    }
}
