package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OptionRepositoryTest {
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    private Product product1;
    private Option option1;



    @BeforeEach
    void setUp() throws Exception {
        optionRepository.deleteAll();
        categoryRepository.save( new Category("테스트1", "#000000", "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg", ""));
        product1 = productRepository.save(new Product("커피", 10000,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",  categoryRepository.findByName("테스트1").get()));
        option1 = optionRepository.save(new Option(product1, "옵션1", 100));

    }

    @AfterEach
    void tearDown() throws Exception {
        optionRepository.deleteAll();
    }


    @Test
    void existsByProduct() {
        assertTrue(optionRepository.existsByProduct(product1));
    }

    @Test
    void existsByProductAndName() {
        assertTrue(optionRepository.existsByProductAndName(product1, "옵션1"));
    }

    @Test
    void findByIdAndProductId() {
        assertThat(optionRepository.findByIdAndProductId(option1.getId(), product1.getId())).isNotEmpty();
    }

    @Test
    void findById() {
        assertThat(optionRepository.findById(option1.getId())).isNotEmpty();
    }

    @Test
    void findAllByProductId() {
        assertThat(optionRepository.findAllByProductId(product1.getId())).isNotEmpty();
    }

    @Test
    void countByProduct() {
        assertThat(optionRepository.countByProduct(product1)).isEqualTo(1);
    }

    @Test
    void deleteAllByProductId() {
        optionRepository.deleteAllByProductId(product1.getId());
        assertThat(optionRepository.findAllByProductId(product1.getId()).size()).isEqualTo(0);
    }
}