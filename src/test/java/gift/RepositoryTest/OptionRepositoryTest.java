package gift.RepositoryTest;

import gift.domain.Category;
import gift.domain.Menu;
import gift.domain.Option;
import gift.repository.MenuRepository;
import gift.repository.OptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private MenuRepository menuRepository;

    private Option option;
    private  Menu menu;

    @BeforeEach
    void setUp() {
        Category category1 = new Category(null, "양식", new LinkedList<Menu>());
        menu = new Menu("파스타", 3000, "naver.com", category1,new HashSet<>());
        option = new Option(null, "검정색", 100L,menu);
        optionRepository.deleteAll();
    }

    @Test
    @DisplayName("옵션 저장 테스트")
    void testSaveOption() {
        Option savedOption = optionRepository.save(option);

        assertThat(savedOption).isNotNull();
        assertThat(savedOption.getId()).isNotNull();
    }

    @Test
    @DisplayName("옵션 findById 테스트")
    void testFindOptionById() {
        Option savedOption = optionRepository.save(option);

        Optional<Option> foundOption = optionRepository.findById(savedOption.getId());
        assertThat(foundOption).isPresent();
        assertThat(foundOption.get().getName()).isEqualTo("검정색");
    }

    @Test
    @DisplayName("옵션 삭제 테스트")
    void testDeleteOption() {
        Option savedOption = optionRepository.save(option);

        optionRepository.deleteById(savedOption.getId());

        Optional<Option> deletedOption = optionRepository.findById(savedOption.getId());
        assertThat(deletedOption).isNotPresent();
    }

    @Test
    @DisplayName("옵션 동일성 테스트")
    void testEquals() {
        Option option1 = new Option(null, "검정색", 50L,menu);
        optionRepository.save(option1);

        Option option2 = new Option(null, "검정색", 60L,menu);
        optionRepository.save(option2);

        Set<Option> optionSet = new HashSet<>();
        optionSet.add(option1);
        optionSet.add(option2);
        assertThat(optionSet).hasSize(1);
    }
}
