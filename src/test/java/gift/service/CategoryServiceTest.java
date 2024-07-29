package gift.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import gift.exception.category.DuplicateCategoryException;
import gift.exception.category.NotFoundCategoryException;
import gift.model.Category;
import gift.repository.CategoryRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @MockBean
    private CategoryRepository categoryRepository;

    @DisplayName("카테고리 생성 테스트")
    @Test
    void save() {
        //given
        String name = "카테고리";
        given(categoryRepository.save(any(Category.class)))
            .willReturn(new Category(1L, name));
        //when
        categoryService.addCategory(name);
        //then
        then(categoryRepository).should().save(any(Category.class));
    }

    @DisplayName("카테고리 중복 생성 테스트")
    @Test
    void failSave() {
        //given
        String name = "이미 존재하는 카테고리";
        given(categoryRepository.findByName(name))
            .willThrow(DuplicateCategoryException.class);
        //when then
        assertThatThrownBy(() -> categoryService.addCategory(name))
            .isInstanceOf(DuplicateCategoryException.class);
    }

    @Test
    @DisplayName("카테고리 전체 조회 테스트")
    void findAll() {
        // given
        given(categoryRepository.findAll())
            .will(invocationOnMock -> Collections.emptyList());
        // when
        categoryService.getAllCategories();
        // then
        then(categoryRepository).should().findAll();
    }

    @Test
    @DisplayName("카테고리 단건 조회 테스트")
    void findById() {
        //given
        Long id = 1L;
        String name = "카테고리";
        given(categoryRepository.findById(any(Long.class)))
            .willReturn(Optional.of(new Category(any(Long.class), name)));
        //when
        categoryService.getCategory(id);
        //then
        then(categoryRepository).should().findById(id);
    }

    @Test
    @DisplayName("카테고리 단건 조회 실패 테스트")
    void failFindById() {
        //given
        Long notExistId = 432L;
        given(categoryRepository.findById(any(Long.class)))
            .willThrow(NotFoundCategoryException.class);
        //when
        assertThatThrownBy(() -> categoryService.getCategory(notExistId))
            .isInstanceOf(NotFoundCategoryException.class);
        //then
    }

    @Test
    @DisplayName("카테고리 변경 테스트")
    void update() {
        //given
        Category savedCategory = mock(Category.class);
        String newName = "변경된 카테고리";
        given(categoryRepository.findById(any(Long.class)))
            .willReturn(Optional.of(savedCategory));
        //when
        categoryService.updateCategory(1L, newName);
        //then
        then(categoryRepository).should().findById(1L);
        then(savedCategory).should().updateCategory(newName);
    }

    @Test
    @DisplayName("카테고리 변경 실패 테스트")
    void failUpdate() {
        //given
        String newName = "변경된 카테고리";
        given(categoryRepository.findById(any(Long.class)))
            .willReturn(Optional.empty());
        //when //then
        assertThatThrownBy(() -> categoryService.updateCategory(1L, newName))
            .isInstanceOf(NotFoundCategoryException.class);
    }

    @Test
    @DisplayName("카테고리 삭제 테스트")
    void delete() {
        //given
        Long id = 1L;
        Category savedCategory = mock(Category.class);
        given(categoryRepository.findById(any(Long.class)))
            .willReturn(Optional.of(savedCategory));
        //when
        categoryService.deleteCategory(id);
        //then
        then(categoryRepository).should().findById(id);
        then(categoryRepository).should().delete(savedCategory);
    }




}