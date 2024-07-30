package gift.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.Category;
import gift.repository.JpaCategoryRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaCategoryRepositoryTest {

    @Autowired
    private JpaCategoryRepository jpaCategoryRepository;

    private Category category;

    private Long insertCategory(Category category) {
        return jpaCategoryRepository.save(category).getId();
    }

    @BeforeEach
    void setCategory(){
        category = new Category("상품권", "#6bd95", "www.naver.com", "");
    }

    @Test
    void 카테고리_추가(){
        //given
        //when
        Long insertCategoryId = insertCategory(category);
        Category findCategory = jpaCategoryRepository.findById(insertCategoryId).get();
        //then
        assertAll(
            () -> assertThat(findCategory).isEqualTo(category)
        );
    }

    @Test
    void 카테고리_단일조회(){
        //given
        Long insertCategoryId = insertCategory(category);
        //when
        Category findCategory = jpaCategoryRepository.findById(insertCategoryId).get();
        //then
        assertAll(
            () -> assertThat(findCategory.getId()).isNotNull(),
            () -> assertThat(findCategory.getId()).isEqualTo(insertCategoryId),
            () -> assertThat(findCategory.getName()).isEqualTo("상품권"),
            () -> assertThat(findCategory.getColor()).isEqualTo("#6bd95"),
            () -> assertThat(findCategory.getImageUrl()).isEqualTo("www.naver.com"),

            () -> assertThrows(NoSuchElementException.class,
                () -> jpaCategoryRepository.findById(100L).get())

        );
    }

    @Test
    void 카테고리_전체조회(){
        //given
        Category category1 = new Category("상품권", "#6bd95", "www.naver.com", "");
        Category category2 = new Category("화장품", "#a8d3d", "www.daum.net", "아아아아");
        insertCategory(category1);
        insertCategory(category2);
        //when
        List<Category> categoryList = jpaCategoryRepository.findAll();
        //then
        assertAll(
            () -> assertThat(categoryList).hasSize(2)
        );
    }

    @Test
    void 카테고리_수정(){
        //given
        insertCategory(category);
        //when
        category.update("화장품", "#a8d3d", "www.daum.net", "아아아아");
        //then
        assertAll(
            () -> assertThat(category.getName()).isEqualTo("화장품"),
            () -> assertThat(category.getColor()).isEqualTo("#a8d3d"),
            () -> assertThat(category.getImageUrl()).isEqualTo("www.daum.net"),
            () -> assertThat(category.getDescription()).isEqualTo("아아아아")
        );
    }

    @Test
    void 카테고리_삭제(){
        //given
        Long insertCategoryId = insertCategory(category);
        //when
        Category findCategory = jpaCategoryRepository.findById(insertCategoryId).get();
        jpaCategoryRepository.delete(findCategory);
        //then
        List<Category> categoryList = jpaCategoryRepository.findAll();
        assertAll(
            () -> assertThat(categoryList).hasSize(0)
        );
    }
}