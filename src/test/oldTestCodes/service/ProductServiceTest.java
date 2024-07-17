//package gift.service;
//
//import gift.entity.Product;
//import gift.service.ProductService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
//import org.springframework.context.annotation.ComponentScan;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@JdbcTest
//@ComponentScan(basePackages = "gift.service")
//class ProductServiceTest {
//
//    @Autowired
//    private ProductService productService;
//
//    @BeforeEach
//    void setUp() {
//        // 데이터 초기화 코드가 필요할 경우 추가
//    }
//
//    @Test
//    void getAllProducts() {
//        List<Product> products = productService.getAllProducts();
//        assertEquals(2, products.size());
//    }
//
//    @Test
//    void addProduct() {
//        Product newProduct = new Product.Builder()
//                .name("New Product")
//                .price(3000)
//                .imageUrl("https://example.com/newproduct.jpg")
//                .build();
//        productService.addProduct(newProduct);
//
//        List<Product> products = productService.getAllProducts();
//        assertEquals(3, products.size());
//        assertTrue(products.stream().anyMatch(product -> "New Product".equals(product.getName())));
//    }
//
//    @Test
//    void updateProduct() {
//        Product updatedProduct = new Product.Builder()
//                .id(1L)
//                .name("Updated Product")
//                .price(1500)
//                .imageUrl("https://example.com/updatedproduct.jpg")
//                .build();
//        productService.updateProduct(1L, updatedProduct);
//
//        Product product = productService.getProductById(1L);
//        assertEquals("Updated Product", product.getName());
//        assertEquals(1500, product.getPrice());
//    }
//
//    @Test
//    void deleteProduct() {
//        productService.deleteProduct(1L);
//
//        List<Product> products = productService.getAllProducts();
//        assertEquals(1, products.size());
//        assertFalse(products.stream().anyMatch(product -> product.getId().equals(1L)));
//    }
//}
