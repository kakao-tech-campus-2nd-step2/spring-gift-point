package gift.repository;

import static org.assertj.core.api.Assertions.as;
import static org.junit.jupiter.api.Assertions.*;

import gift.entity.Category;
import gift.entity.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void save() {
        //given
        Category category = new Category("카테고리");
        Product expected = new Product("product", 100, "image.jpg", category);

        //when
        Product actual = productRepository.save(expected);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo("product"),
            () -> assertThat(actual.getPrice()).isEqualTo(100),
            () -> assertThat(actual.getImageUrl()).isEqualTo("image.jpg"),
            () -> assertThat(actual.getCategory()).isEqualTo(category)
        );
    }

    @Test
    void findAll() {
        //given
        Category category1 = new Category("카테고리1");
        Category category2 = new Category("카테고리2");
        Product expected = new Product("product", 100, "image.jpg", category1);
        Product expected1 = new Product("product1", 1000, "image1.jpg", category2);
        productRepository.save(expected);
        productRepository.save(expected1);

        //when
        List<Product> actual = productRepository.findAll();

        //then
        assertAll(
            () -> assertThat(actual.size()).isEqualTo(2),
            () -> assertThat(actual).containsExactlyInAnyOrder(expected, expected1)

        );

    }

    @Test
    void findById() {
        //given
        Category category = new Category("카테고리");
        Product expected = new Product("product", 100, "image.jpg", category);
        productRepository.save(expected);

        //when
        Optional<Product> actual = productRepository.findById(expected.getId());

        //then
        assertAll(
            () -> assertThat(actual).isPresent(),
            () -> assertThat(actual.get().getName()).isEqualTo("product"),
            () -> assertThat(actual.get().getPrice()).isEqualTo(100),
            () -> assertThat(actual.get().getImageUrl()).isEqualTo("image.jpg"),
            () -> assertThat(actual.get().getCategory()).isEqualTo(category)
        );

    }

    @Test
    void update() {
        //given
        Category category = new Category("카테고리");
        Product expected = new Product("product", 100, "image.jpg",category);
        Product updateProduct = productRepository.save(expected);
        Product productFromDB = productRepository.findById(updateProduct.getId()).get();
        expected.setName("updated");
        expected.setPrice(1000);
        expected.setImageUrl("updatedImage.jpg");
        expected.setCategory(new Category("category"));

        //when
        Product actual = productRepository.save(productFromDB);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(updateProduct.getId()),
            () -> assertThat(actual.getName()).isEqualTo("updated"),
            () -> assertThat(actual.getPrice()).isEqualTo(1000),
            () -> assertThat(actual.getImageUrl()).isEqualTo("updatedImage.jpg"),
            ()->assertThat(actual.getCategory().getName()).isEqualTo("category")
        );
    }

    @Test
    void deleteById() {
        //given
        Category category = new Category("카테고리");
        Product product = new Product("product", 100, "image.jpg",category);
        productRepository.save(product);
        Long id = product.getId();

        //when
        productRepository.deleteById(id);
        Optional<Product> expected = productRepository.findById(id);

        assertThat(expected.isPresent()).isFalse();

    }


}