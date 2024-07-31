package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.entity.Category;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("상품추가 테스트")
    public void addProductTest() {
        Category category = new Category("카테고리", "컬러", "이미지", "설명");
        categoryRepository.save(category);

        Product product = new Product("치킨", 20000, "chicken.com", category);
        productRepository.save(product);

        Optional<Product> findProduct = productRepository.findById(product.getId());
        assertThat(findProduct).isPresent();
        assertThat(findProduct.get().getName()).isEqualTo(product.getName());
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    public void deleteProductTest() {
        Category category = new Category("카테고리", "컬러", "이미지", "설명");
        categoryRepository.save(category);

        Product product = new Product("치킨", 20000, "chicken.com", category);
        productRepository.save(product);

        productRepository.deleteById(product.getId());

        Optional<Product> findProduct = productRepository.findById(product.getId());
        assertThat(findProduct).isEmpty();
    }

    @Test
    public void save() {
        Category category = new Category("카테고리", "컬러", "이미지", "설명");
        categoryRepository.save(category);

        Product expected = new Product("치킨", 20000, "chicken.com", category);
        Product actual = productRepository.save(expected);
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    public void findByName() {
        Category category = new Category("카테고리", "컬러", "이미지", "설명");
        categoryRepository.save(category);

        String expected = "치킨";
        productRepository.save(new Product("치킨", 20000, "chicken.com", category));
        boolean exists = productRepository.existsByName(expected);
        assertThat(exists).isTrue();
    }

    @Test
    public void findAllPagingTest(){
        Category category = new Category("카테고리", "컬러", "이미지", "설명");
        categoryRepository.save(category);

        for(int i=0; i<50; i++){
            Product product = new Product("name"+i,1000*i, i+".com", category);
            productRepository.save(product);
        }

        Pageable pageable = PageRequest.of(0,10);
        Page<Product> page = productRepository.findAll(pageable);

        assertThat(page.getTotalElements()).isEqualTo(50);
        assertThat(page.getTotalPages()).isEqualTo(5);
        assertThat(page.getContent().size()).isEqualTo(10);
    }


}
