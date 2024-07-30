package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.dto.OptionEditRequest;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql("/sql/truncateIdentity.sql")
class OptionJpaDaoTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private CategoryJpaDao categoryJpaDao;
    @Autowired
    private ProductJpaDao productJpaDao;
    @Autowired
    private OptionJpaDao optionJpaDao;

    private final Category category = new Category("음식", "Red", "http", "description");
    private final Product product = new Product("케익", 3500L, "http", category);

    @BeforeEach
    void setUp() {
        categoryJpaDao.save(category);
        productJpaDao.save(product);
    }

    @Test
    @DisplayName("옵션 저장 테스트")
    void save() {
        Option option = optionJpaDao.save(new Option("초코 케익", 77, product));

        assertThat(option.getName()).isEqualTo("초코 케익");
    }

    @Test
    @DisplayName("id로 옵션 조회 테스트")
    void findById() {
        Option option = optionJpaDao.save(new Option("초코 케익", 77, product));

        assertThat(optionJpaDao.findById(option.getId()).get()).isSameAs(option);
    }

    @Test
    @DisplayName("옵션 수정 테스트")
    void optionUpdate() {
        Option option = optionJpaDao.save(new Option("초코 케익", 77, product));
        OptionEditRequest updatedOption = new OptionEditRequest(option.getId(), "딸기 케익", 10, null);
        option.updateOption(updatedOption);

        entityManager.flush();

        assertThat(optionJpaDao.findById(option.getId()).get().getName()).isEqualTo("딸기 케익");
    }

    @Test
    @DisplayName("옵션 삭제 테스트")
    void optionDelete() {
        Option option = optionJpaDao.save(new Option("초코 케익", 77, product));
        optionJpaDao.deleteById(option.getId());

        entityManager.flush();

        assertThat(optionJpaDao.findById(option.getId())).isEqualTo(Optional.empty());
    }
}