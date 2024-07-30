package gift.service;

import gift.dto.CategoryRequestDto;
import gift.dto.CategoryResponseDto;
import gift.exception.BusinessException;
import gift.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    public void 카테고리_추가_성공() {
        CategoryRequestDto requestDto = new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리");
        CategoryResponseDto responseDto = categoryService.addCategory(requestDto);

        assertNotNull(responseDto);
        assertNotNull(responseDto.getId());
        assertEquals("테스트카테고리", responseDto.getName());
        assertEquals("#FF0000", responseDto.getColor());
        assertEquals("https://example.com/test.png", responseDto.getImageUrl());
        assertEquals("테스트 카테고리", responseDto.getDescription());
    }

    @Test
    public void 카테고리_수정_성공() {
        CategoryResponseDto responseDto = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리"));
        Long categoryId = responseDto.getId();

        CategoryRequestDto updateDto = new CategoryRequestDto("수정된카테고리", "#00FF00", "https://example.com/test2.png", "수정된 카테고리");
        CategoryResponseDto updatedResponseDto = categoryService.updateCategory(categoryId, updateDto);

        assertNotNull(updatedResponseDto);
        assertEquals(categoryId, updatedResponseDto.getId());
        assertEquals("수정된카테고리", updatedResponseDto.getName());
        assertEquals("#00FF00", updatedResponseDto.getColor());
        assertEquals("https://example.com/test2.png", updatedResponseDto.getImageUrl());
        assertEquals("수정된 카테고리", updatedResponseDto.getDescription());
    }

    @Test
    public void 모든_카테고리_조회_성공() {
        categoryService.addCategory(new CategoryRequestDto("테스트카테고리1", "#FF0000", "https://example.com/test1.png", "테스트 카테고리1"));
        categoryService.addCategory(new CategoryRequestDto("테스트카테고리2", "#00FF00", "https://example.com/test2.png", "테스트 카테고리2"));

        List<CategoryResponseDto> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        assertEquals("테스트카테고리1", categories.get(0).getName());
        assertEquals("테스트카테고리2", categories.get(1).getName());
    }

    @Test
    public void 카테고리_조회_성공() {
        CategoryResponseDto responseDto = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리"));
        Long categoryId = responseDto.getId();

        CategoryResponseDto category = categoryService.getCategoryById(categoryId);

        assertNotNull(category);
        assertEquals(categoryId, category.getId());
        assertEquals("테스트카테고리", category.getName());
        assertEquals("#FF0000", category.getColor());
        assertEquals("https://example.com/test.png", category.getImageUrl());
        assertEquals("테스트 카테고리", category.getDescription());
    }

    @Test
    public void 카테고리_삭제_성공() {
        CategoryResponseDto responseDto = categoryService.addCategory(new CategoryRequestDto("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리"));
        Long categoryId = responseDto.getId();

        categoryService.deleteCategory(categoryId);

        assertThrows(BusinessException.class, () -> categoryService.getCategoryById(categoryId));
    }
}
