package gift.RepositoryTest;

import gift.Exception.ProductNotFoundException;
import gift.Model.Entity.Category;
import gift.Model.Entity.Product;
import gift.Repository.CategoryRepository;
import gift.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    void beforeEach() {
        category = categoryRepository.save(new Category("카테고리", "#812f3D", "카테고리 url", "카테고리 description"));
    }

    @Test
    void saveTest(){
        Product product = new Product("아메리카노", 4000,"아메리카노url", category);
        assertThat(product.getId()).isNull();
        var actual = productRepository.save(product);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void findByIdTest(){
        Product product = productRepository.save(new Product("아메리카노", 4000, "아메리카노url", category));
        Optional<Product> actual = productRepository.findById(product.getId());
        assertThat(actual.get().getName().getValue()).isEqualTo("아메리카노");
    }

    @Test
    void updateTest(){
        Product product1 = productRepository.save(new Product("아메리카노", 4000, "아메리카노url", category));
        Optional<Product> optionalProduct = productRepository.findById(product1.getId());
        Product product = optionalProduct.get();
        product.update("카푸치노", 5000, "카푸치노url", category);

        Product actual = productRepository.findById(product.getId()).orElseThrow(() -> new ProductNotFoundException("매칭되는 product가 없습니다."));
        assertAll(
                () -> assertThat(actual.getName().getValue()).isEqualTo("카푸치노"),
                () -> assertThat(actual.getPrice().getValue()).isEqualTo(5000),
                () -> assertThat(actual.getImageUrl().getValue()).isEqualTo("카푸치노url"),
                () -> assertThat(actual.getCategory()).isEqualTo(category)
        );
        assertThat(actual.getName().getValue()).isEqualTo("카푸치노");
    }

    @Test
    void deleteTest(){
        Product product1 = productRepository.save(new Product("아메리카노", 4000, "아메리카노url", category));
        Optional<Product> optionalProduct = productRepository.findById(product1.getId());
        productRepository.deleteById(optionalProduct.get().getId());
        Optional<Product> actual  = productRepository.findById(optionalProduct.get().getId());
        assertThat(actual).isEmpty();
    }
}
