package gift.RepositoryTest;

import gift.Model.Entity.*;
import gift.Repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class OptionEntityRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save(){
        ProductEntity productEntity = productRepository.findById(1L).get();
        OptionEntity expected = new OptionEntity(productEntity, "testName", 1L);
        OptionEntity actual = optionRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void findByName() {
        ProductEntity productEntity = productRepository.findById(1L).get();
        OptionEntity expected = new OptionEntity(productEntity, "testName", 1L);
        optionRepository.save(expected);

        OptionEntity actual = optionRepository.findByName(expected.getName()).get();

        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @Test
    void findByProductId(){
        ProductEntity productEntity = productRepository.findById(1L).get();

        OptionEntity expected1 = new OptionEntity(productEntity, "testName1", 1L);
        OptionEntity expected2 = new OptionEntity(productEntity, "testName2", 1L);

        optionRepository.save(expected1);
        optionRepository.save(expected2);

        List<OptionEntity> expected = new ArrayList<>();
        List<OptionEntity> actual = optionRepository.findByProductId(productEntity.getId());
        assertThat(actual.getLast().getName()).isEqualTo(expected2.getName());
    }
}
