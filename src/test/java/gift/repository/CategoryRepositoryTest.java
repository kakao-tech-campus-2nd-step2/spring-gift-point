package gift.repository;

import gift.domain.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void saveTest(){
        // when
        Category category = new Category("new");
        Category actual = categoryRepository.save(category);
        // then
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo(category.getName());
    }

    @Test
    void findAllTest(){
        // when
        int pageNumber = 0;
        int pageSize = 5;
        Long sum = 0L;
        Long count = 5L;

        while(count.intValue() == pageSize){
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Category> categories = categoryRepository.findAll(pageable);

            Assertions.assertThat(categories).isNotNull();

            count = categories.get().count();
            sum += count;

            pageNumber++;
        }

        // then
        Assertions.assertThat(sum).isEqualTo(13L);
    }

    @Test
    void findByIdTest(){
        // when
        Category category = new Category("분류1");
        categoryRepository.save(category);
        Category actual = categoryRepository.findById(14L).orElseThrow();
        // then
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getName()).isEqualTo("분류1");
    }

    @Test
    void deleteTest(){
        // given
        Category category = new Category("new");
        Category actual = categoryRepository.save(category);
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getId()).isEqualTo(14L);
        // when
        categoryRepository.deleteById(14L);
        // then
        boolean tf = categoryRepository.existsById(14L);
        Assertions.assertThat(tf).isEqualTo(false);
    }
}
