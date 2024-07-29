package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.domain.Option;
import gift.domain.Product;
import gift.repository.fixture.OptionFixture;
import gift.repository.fixture.ProductFixture;
import gift.service.OptionService;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataJpaTest
public class OptionRepositoryTest {
    @Autowired
    private OptionRepository optionRepository;
    @MockBean
    private OptionService optionService;
    @Autowired
    private EntityManager em;

    @Test
    void 옵션_추가(){
        // given
        Option expected = OptionFixture.createOption("test",10);
        // when
        Option actual = optionRepository.save(expected);
        em.flush();
        em.clear();
        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull()
        );
    }

    @Test
    void 특정_옵션_검색(){
        // given
        Option expected = OptionFixture.createOption("test",10);
        optionRepository.save(expected);
        em.flush();
        em.clear();
        // when
        Option actual = optionRepository.findById(expected.getId()).get();
        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(expected.getId())
        );
    }

    @Test
    void 특정_옵션_삭제(){
        // given
        Option expected = OptionFixture.createOption("test",10);
        optionRepository.save(expected);
        em.flush();
        em.clear();
        // when
        optionRepository.deleteById(expected.getId());
        Optional<Option> actual = optionRepository.findById(expected.getId());
        // then
        assertThat(actual).isEmpty();
    }
}
