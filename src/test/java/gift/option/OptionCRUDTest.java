package gift.option;

import gift.category.model.Category;
import gift.category.repository.CategoryRepository;
import gift.option.domain.Option;
import gift.option.repository.OptionJpaRepository;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OptionCRUDTest {

    @Autowired
    private OptionJpaRepository optionJpaRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        optionJpaRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @Transactional
    void 옵션_create_테스트() {
        // Given
        Category category = new Category("cake");
        categoryRepository.save(category);
        Product product = new Product("Cheese cake", 7000, "imageUrl", category);
        productRepository.save(product);

        Option option = new Option("아메리칸 정통 스타일", 1L, product);

        // When
        Option savedOption = optionJpaRepository.save(option);

        // Then
        assertThat(savedOption).isNotNull();
        assertThat(savedOption.getName()).isEqualTo("아메리칸 정통 스타일");
        assertThat(savedOption.getQuantity()).isEqualTo(1L);
        assertThat(savedOption.getProduct()).isEqualTo(product);
    }

    @Test
    @Transactional
    void 옵션_read_테스트() {
        // Given
        Category category = new Category("cake");
        categoryRepository.save(category);
        Product product = new Product("Cheese cake", 7000, "imageUrl", category);
        productRepository.save(product);

        Option option0 = new Option("아이스크림 추가", 1L, product);
        Option option1 = new Option("시럽 추가",2L, product);
        optionJpaRepository.save(option0);
        optionJpaRepository.save(option1);

        // When
        List<Option> options = optionJpaRepository.findAllByProduct(product);

        // Then
        assertThat(options).isNotEmpty();
        assertThat(options).hasSize(2);
        assertThat(options).isEqualTo(List.of(option0, option1));
    }

    @Test
    @Transactional
    void 옵션_업데이트_테스트() {
        // Given
        Category category = new Category("cake");
        categoryRepository.save(category);
        Product product = new Product("Cheese cake", 7000, "imageUrl", category);
        productRepository.save(product);

        Option option = new Option("아메리칸 정통 스타일", 1L, product);
        optionJpaRepository.save(option);

        // When
        option.update("한국 스타일", 200L);
        Option updatedOption = optionJpaRepository.save(option);

        // Then
        assertThat(updatedOption.getName()).isEqualTo("한국 스타일");
        assertThat(updatedOption.getQuantity()).isEqualTo(200L);
    }

    @Test
    @Transactional
    void 옵션_삭제_테스트() {
        // Given
        Category category = new Category("cake");
        categoryRepository.save(category);
        Product product = new Product("Cheese cake", 7000, "imageUrl", category);
        productRepository.save(product);

        Option option = new Option("아메리칸 정통 스타일", 1L, product);
        optionJpaRepository.save(option);

        // When
        optionJpaRepository.delete(option);
        Option deletedOption = optionJpaRepository.findById(option.getId()).orElse(null);

        // Then
        assertThat(deletedOption).isNull();
    }

    @Test
    @Transactional
    void 옵션_중복_생성_테스트() {
        // Given
        Category category = new Category("cake");
        categoryRepository.save(category);
        Product product = new Product("Cheese cake", 7000, "imageUrl", category);
        productRepository.save(product);

        Option option0 = new Option("아메리칸 정통 스타일", 1L, product);
        optionJpaRepository.save(option0);
        Option option1 = new Option("아메리칸 정통 스타일", 1L, product);
        optionJpaRepository.save(option1);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            if (optionJpaRepository.findAllByProduct(product).stream()
                    .anyMatch(option -> option0.getName().equals(option1.getName()))) {
                throw new IllegalArgumentException("[ERROR] 중복된 옵션 존재 : " + option1.getName());
            }
            optionJpaRepository.save(option1);
        });
    }
}
