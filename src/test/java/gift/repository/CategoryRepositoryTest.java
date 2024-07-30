package gift.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Category;
import gift.exception.DataNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void testSaveCategory() {
        //given
        Category expected = new Category("카테고리");

        //when
        Category actual = categoryRepository.save(expected);

        //then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getName()).isEqualTo("카테고리")
        );
    }

    @Test
    void findById() {
        //given
        Category expected = new Category("카테고리");
        categoryRepository.save(expected);

        //when
        Category actual = categoryRepository.findById(expected.getId()).orElseThrow(
            () -> new DataNotFoundException("")
        );

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );


    }

    @Test
    void findAll() {
        //given

        List<String> expected = Arrays.asList("교환권", "상품권", "뷰티", "패션", "식품", "리빙/도서", "레저/스포츠",
            "아티스트/캐릭터", "유아동/반려", "디지털/가전", "카카오프렌즈", "트렌드 선물", "백화점"
        );
        //when
        List<Category> actual = categoryRepository.findAll();
        List<String> actualNames = actual.stream()
            .map(Category::getName)
            .collect(Collectors.toList());

        //then
        Collections.sort(actualNames);
        Collections.sort(expected);
        assertEquals(actualNames, expected);
    }
}