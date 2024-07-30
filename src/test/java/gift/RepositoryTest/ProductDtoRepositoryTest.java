package gift.RepositoryTest;

import gift.Entity.Category;
import gift.Entity.Product;
import gift.Repository.CategoryJpaRepository;
import gift.Repository.ProductJpaRepository;
import gift.Service.CategoryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductDtoRepositoryTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @BeforeEach
    void setUp() {
        Category category = new Category(1L, "category1");
        categoryJpaRepository.save(category);
    }

    @Test
    public void testGetAllProducts() {
        //given
        Category category1 = categoryJpaRepository.findById(1L).get();
        Product product1 = new Product(1L, "product1", category1, 1000, "http://localhost:8080/image1.jpg", false);
        productJpaRepository.save(product1);

        Product product2 = new Product(2L, "product2", category1, 2000, "http://localhost:8080/image2.jpg", false);
        productJpaRepository.save(product2);

        List<Product> productslist = productJpaRepository.findByisDeletedFalse();

        assertThat(productslist.get(0).getId()).isEqualTo(product1.getId());
        assertThat(productslist.get(0).getName()).isEqualTo(product1.getName());
        assertThat(productslist.get(0).getCategory().getCategoryId()).isEqualTo(category1.getCategoryId());
        assertThat(productslist.get(0).getPrice()).isEqualTo(product1.getPrice());
        assertThat(productslist.get(0).getImageUrl()).isEqualTo(product1.getImageUrl());
        assertThat(productslist.get(0).isDeleted()).isEqualTo(product1.isDeleted());

        assertThat(productslist.get(1).getId()).isEqualTo(product2.getId());
        assertThat(productslist.get(1).getName()).isEqualTo(product2.getName());
        assertThat(productslist.get(1).getCategory().getCategoryId()).isEqualTo(category1.getCategoryId());
        assertThat(productslist.get(1).getPrice()).isEqualTo(product2.getPrice());
        assertThat(productslist.get(1).getImageUrl()).isEqualTo(product2.getImageUrl());
        assertThat(productslist.get(1).isDeleted()).isEqualTo(product2.isDeleted());

    }

    @Test
    public void testGetProductById() {
        Category category = categoryJpaRepository.findById(1L).get();
        Product product = new Product(1L, "product1", category, 1000, "http://localhost:8080/image1.jpg", false);

        Product savedProduct = productJpaRepository.save(product);

        assertThat(savedProduct.getId()).isEqualTo(product.getId());
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(savedProduct.getImageUrl()).isEqualTo(product.getImageUrl());
        assertThat(savedProduct.isDeleted()).isEqualTo(product.isDeleted());

    }

    @Test
    public void testSaveProduct() {
        Category category = categoryJpaRepository.findById(1L).get();
        Product product = new Product(1L, "product1", category, 1000, "http://localhost:8080/image1.jpg", false);

        Product savedProduct = productJpaRepository.save(product);

        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());

    }

    @Test
    public void testUpdateProduct() {
        Category category = categoryJpaRepository.findById(1L).get();
        Product product1 = new Product(1L, "product1", category, 1000, "http://localhost:8080/image1.jpg", false);

        productJpaRepository.save(product1);

        product1.setName("productDto2");
        product1.setPrice(2000);
        product1.setImageUrl("http://localhost:8080/image2.jpg");

        Product product2 = product1;

        Product savedProduct = productJpaRepository.save(product2);

        // product1을 update한 후 product2로 저장한 것이므로 product1의 id와 product2의 id는 같아야 한다.
        assertThat(savedProduct.getId()).isEqualTo(product1.getId());

    }

    @Test
    public void testDeleteProduct() {
        Category category = categoryJpaRepository.findById(1L).get();
        Product product = new Product(1L, "product1", category, 1000, "http://localhost:8080/image1.jpg", false);

        productJpaRepository.save(product);

        productJpaRepository.delete(product);

        assertThat(productJpaRepository.findById(product.getId())).isEmpty();
    }
}