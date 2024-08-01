package gift.Service;

import gift.DTO.ProductDTO;
import gift.Entity.ProductEntity;
import gift.Repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    public ProductServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        ProductEntity product1 = new ProductEntity(1L, "Product1", 100, "url1");
        ProductEntity product2 = new ProductEntity(2L, "Product2", 200, "url2");
        Page<ProductEntity> productPage = new PageImpl<>(Arrays.asList(product1, product2), pageable, 2);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductDTO> result = productService.getProducts(pageable);

        assertProductPage(productPage, result);
    }

    private void assertProductPage(Page<ProductEntity> expectedPage, Page<ProductDTO> actualPage) {
        assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
        assertEquals(expectedPage.getContent().size(), actualPage.getContent().size());
        for (int i = 0; i < expectedPage.getContent().size(); i++) {
            assertEquals(expectedPage.getContent().get(i).getName(), actualPage.getContent().get(i).getName());
            assertEquals(expectedPage.getContent().get(i).getPrice(), actualPage.getContent().get(i).getPrice());
            assertEquals(expectedPage.getContent().get(i).getImageUrl(), actualPage.getContent().get(i).getImageUrl());
        }
    }
}
