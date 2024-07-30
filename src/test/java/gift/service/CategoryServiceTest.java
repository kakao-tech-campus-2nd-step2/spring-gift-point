package gift.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Category;
import gift.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category = new Category("Category1");
    private Category category1 = new Category("Category2");


    @Test
    void findAllTest() {
        //given
        List<Category> list = new ArrayList<>();
        list.add(category);
        list.add(category1);
        when(categoryRepository.findAll()).thenReturn(list);

        //when
        List<Category> actual = categoryService.findAll();

        //then
        assertThat(actual.size()).isEqualTo(2);

    }

    @Test
    void saveTest() {
        //given
        when(categoryRepository.save(category)).thenReturn(category);

        //when
        categoryService.save(category);

        //then
        verify(categoryRepository).save(category);

    }

    @Test
    void findByIdTest() {
        //given
        Optional<Category> expected = Optional.of(category);
        when(categoryRepository.findById(any())).thenReturn(expected);

        //when
        Category actual = categoryService.findById(1L);

        //then
        assertThat(actual.getName()).isEqualTo("Category1");


    }
}