package gift.repositoryTest;

import gift.model.Option;
import gift.repository.OptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OptionRepositoryTests {

    @Autowired
    private OptionRepository optionRepository;

    @Test
    void testSaveAndFindOption() {
        Option option = new Option("Option1", 10);

        Option savedOption = optionRepository.save(option);
        Optional<Option> foundOption = optionRepository.findById(savedOption.getId());

        assertThat(foundOption).isPresent();
        assertThat(foundOption.get().getName()).isEqualTo("Option1");
    }

    @Test
    void testUpdateOption() {
        Option option = new Option("Option2", 10);
        Option savedOption = optionRepository.save(option);

        savedOption.setName("Option2업데이트");
        Option updatedOption = optionRepository.save(savedOption);

        assertThat(updatedOption.getName()).isEqualTo("Option2업데이트");
    }

    @Test
    void testDeleteOption() {
        Option option = new Option("Option3", 10);
        Option savedOption = optionRepository.save(option);

        optionRepository.deleteById(savedOption.getId());
        Optional<Option> deletedOption = optionRepository.findById(savedOption.getId());

        assertThat(deletedOption).isNotPresent();
    }
}
