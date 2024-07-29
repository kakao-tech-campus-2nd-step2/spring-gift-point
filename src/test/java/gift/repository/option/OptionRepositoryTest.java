package gift.repository.option;

import gift.model.option.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Test
    @DisplayName("옵션이 잘 저장되는지 확인")
    void testSaveOption() {
        //given
        Option option = new Option(null, "testOption1", 1);

        //when
        Option savedOption = optionRepository.save(option);

        //then
        assertThat(savedOption).isNotNull();
        assertThat(savedOption).isSameAs(option);
        assertThat(savedOption.getName()).isEqualTo(option.getName());

    }

    @Test
    @DisplayName("옵션이 잘 조회되는지 확인")
    void testfindOption() {
        //given
        Option option1 = optionRepository.save(new Option(null, "testOption1", 1));
        Option option2 = optionRepository.save(new Option(null, "testOption2", 1));

        //when
        Option findOption1 = optionRepository.findById(option1.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 옵션이 없습니다. id : " + option1.getId()));
        Option findOption2 = optionRepository.findById(option2.getId())
                .orElseThrow(() -> new NoSuchElementException("해당 옵션이 없습니다. id : " + option2.getId()));

        //then
        assertThat(findOption1.getName()).isEqualTo(option1.getName());
        assertThat(findOption2.getName()).isEqualTo(option2.getName());

    }

    @Test
    @DisplayName("옵션이 잘 수정되는지 확인")
    void testUpdateOption() {
        //given
        Option option1 = optionRepository.save(new Option(null, "testOption1", 1));

        //when
        option1.modify("updateOption1", 2);
        Option savedOption1 = optionRepository.save(option1);

        //then
        assertThat(savedOption1.getName()).isEqualTo("updateOption1");
    }

    @Test
    @DisplayName("옵션이 잘 삭제되는지 확인")
    void testDeleteOption() {
        //given
        Option option1 = optionRepository.save(new Option(null, "testOption1", 1));

        //when
        optionRepository.delete(option1);
        Optional<Option> deletedOption = optionRepository.findById(option1.getId());

        //then
        assertThat(deletedOption).isNotPresent();
    }


}