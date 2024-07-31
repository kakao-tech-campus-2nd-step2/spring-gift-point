package gift.repository;

import gift.config.JpaConfig;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("id로 상품 정보 업데이트 테스트[성공]")
    void updateById() {
        // given
        String name = "name1";
        int price = 1000;
        String imageUrl = "imageUrl1";
        String imageUrl2 = "imageUrl2";
        Category category = categoryRepository.save(new Category("name", "#123", "url", ""));

        List<Option> options = List.of(new Option("oName", 123));
        Product product = new Product(name, price, imageUrl, category, options);
        Product original = productRepository.save(product);

        // when
        Product updated = productRepository.findById(original.getId()).orElse(null);
        assert updated != null;
        updated.updateProduct(name, price, imageUrl2, category);
        Product actual = productRepository.save(updated);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getImageUrl()).isEqualTo(imageUrl2);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
        assertThat(actual.getOptions().get(0)).isEqualTo(options.get(0));
    }

    @Test
    @DisplayName("상품 정보 저장 테스트[성공]")
    void save() {
        // given
        String name = "name1";
        int price = 1000;
        String imageUrl = "imageUrl1";
        Category category = categoryRepository.save(new Category("name", "#123", "url", ""));
        List<Option> options = List.of(new Option("oName", 123));
        Product product = new Product(name, price, imageUrl, category, options);

        // when
        Product actual = productRepository.save(product);

        // then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getImageUrl()).isEqualTo(imageUrl);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
        assertThat(actual.getOptions()).hasSize(1);
        assertThat(actual.getOptions().get((0))).isEqualTo(options.get(0));
    }

    @Test
    @DisplayName("카테고리와 fetch join하여 모든 상품 조회 테스트[성공]")
    void findProductAndCategoryFetchJoin() {
        // given
        String[] name = {"name1", "name2"};
        int price = 1000;
        String imageUrl = "imageUrl1";
        Category category = categoryRepository.save(new Category("name", "#123", "url", ""));
        List<Option>[] options = new List[2];
        options[0] = List.of(new Option("oName", 123));
        options[1] = List.of(new Option("oName", 123));

        Product[] product = {
                new Product(name[0], price, imageUrl, category, options[0]),
                new Product(name[1], price, imageUrl, category, options[1])
        };
        productRepository.save(product[0]);
        productRepository.save(product[1]);
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Product> actuals = productRepository.findProductAndCategoryFetchJoin(pageable);

        // then
        assertThat(actuals.getTotalElements()).isEqualTo(2);
        assertThat(actuals.getTotalPages()).isEqualTo(1);
        assertThat(actuals.getNumber()).isEqualTo(0);
        for (int i = 0; i < actuals.getContent().size(); i++) {
            assertThat(actuals.getContent().get(i)).isEqualTo(product[i]);
            assertThat(actuals.getContent().get(i).getOptions().get(0)).isEqualTo(options[i].get(0));
        }
    }

    @Test
    @DisplayName("카테고리 id로 Product 조회 테스트[성공]")
    void findByCategoryId() {
        // given
        String pName = "name1";
        int price = 1000;
        String imageUrl = "imageUrl1";
        Category category = categoryRepository.save(new Category("name", "#123", "url", ""));
        List<Option> options = List.of(new Option("oName", 123));
        Product product = new Product(pName, price, imageUrl, category, options);
        productRepository.save(product);

        // when
        List<Product> products = productRepository.findByCategoryId(product.getCategory().getId());

        // then
        assertThat(products.size()).isEqualTo(1);
        assertThat(products.get(0).getName()).isEqualTo(pName);
        assertThat(products.get(0).getPrice()).isEqualTo(price);
        assertThat(products.get(0).getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("옵선 수량 빼기 작업 테스트[성공]")
    void subtractOptionQuantity() {
        // given
        int quantity = 123;
        int subtractAmount = 10;
        int expectedQuantity = quantity - subtractAmount;
        Category category = categoryRepository.save(new Category("cname", "ccolor", "cImage", ""));
        List<Option> options = List.of(new Option("oName", quantity));
        Product product = productRepository.save(new Product("pName", 0, "purl", category, options));
        Long id = product.getOptions().get(0).getId();
        Option option = product.getOptions().get(0);
        option.subtractQuantity(subtractAmount);

        // when
        product = productRepository.findProductAndOptionByIdFetchJoin(id).get();
        Option actual = product.findOptionByOptionId(product.getOptions().get(0).getId());

        // then
        assertThat(actual.getQuantity()).isEqualTo(expectedQuantity);
    }
}