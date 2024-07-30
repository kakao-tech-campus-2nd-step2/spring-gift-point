package gift;

import gift.model.Product;
import gift.repository.ProductRepository;
import gift.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setup() {
        product = new Product();
        product.setName("Product Name");
        product.setPrice(100);
        product.setImageurl("https://cs.kakao.com/images/icon/img_kakaocs.png");
    }

    @Test
    public void testGetAllProducts() {
        List<Product> productList = Collections.singletonList(product);
        Page<Product> productPage = new PageImpl<>(productList, PageRequest.of(0, 5), 1);
        Mockito.when(productRepository.findAll(Mockito.any(Pageable.class))).thenReturn(productPage);

        ResponseEntity<Page<Product>> response = productService.getAllProducts(PageRequest.of(0, 5));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).getName()).isEqualTo(product.getName());
    }
}