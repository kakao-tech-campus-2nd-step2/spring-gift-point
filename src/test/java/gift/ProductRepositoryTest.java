package gift;

import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save() {

        Category category = new Category();
        category.setName("Food");
        Category savedCategory = categoryRepository.save(category);


        Product product = new Product();
        product.setName("열라면");
        product.setPrice(1600);
        product.setImageurl("https://i.namu.wiki/i/fuvd7qkb8P6PA_sD5ufjgpKUhRgxxTrIWnkPIg5H_UAPMUaArn1U1DweD7T_f_8RVxTDjqaiFwKr-quURwc_eQ.webp");
        product.setCategory(savedCategory);

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("열라면");
        assertThat(savedProduct.getCategory()).isEqualTo(savedCategory);
    }
}