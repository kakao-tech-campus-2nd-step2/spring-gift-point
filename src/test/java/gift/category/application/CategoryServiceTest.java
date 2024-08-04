package gift.category.application;

import gift.category.application.command.CategoryCreateCommand;
import gift.category.application.command.CategoryUpdateCommand;
import gift.category.domain.Category;
import gift.category.domain.CategoryRepository;
import gift.exception.type.DuplicateNameException;
import gift.exception.type.NotFoundException;
import gift.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Long categoryId;
    private Category category;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryId = 1L;
        category = new Category(categoryId, "Category", "#FFFFFF", "Description", "http://example.com/image.jpg");
    }

    @Test
    public void 모든카테고리_조회() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = new PageImpl<>(List.of(category), pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<CategoryServiceResponse> result = categoryService.findAll(pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).name()).isEqualTo("Category");
    }

    @Test
    public void 카테고리_생성_중복된이름_예외발생() {
        // Given
        CategoryCreateCommand command = new CategoryCreateCommand("Category", "#FFFFFF", "Description", "http://example.com/image.jpg");
        when(categoryRepository.findByName(command.name())).thenReturn(Optional.of(command.toCategory()));

        // When & Then
        assertThatThrownBy(() -> categoryService.create(command))
                .isInstanceOf(DuplicateNameException.class)
                .hasMessage("이미 존재하는 카테고리 이름 입니다.");
    }

    @Test
    public void 카테고리_생성_성공() {
        // Given
        CategoryCreateCommand command = new CategoryCreateCommand("Category", "#FFFFFF", "Description", "http://example.com/image.jpg");
        when(categoryRepository.findByName(command.name())).thenReturn(Optional.empty());

        // When
        categoryService.create(command);

        // Then
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void 카테고리_아이디로_조회_존재하지않는_카테고리_예외발생() {
        // Given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.findById(categoryId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 카테고리입니다.");
    }

    @Test
    public void 카테고리_아이디로_조회_성공() {
        // Given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        CategoryServiceResponse result = categoryService.findById(categoryId);

        // Then
        assertThat(result.name()).isEqualTo("Category");
    }

    @Test
    public void 카테고리_수정_존재하지않는_카테고리_예외발생() {
        // Given
        CategoryUpdateCommand command = new CategoryUpdateCommand(categoryId, "Updated Category", "#000000", "Updated Description", "http://example.com/updated-image.jpg");
        when(categoryRepository.findById(command.categoryId())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.update(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 카테고리입니다.");
    }

    @Test
    public void 카테고리_수정_성공() {
        // Given
        CategoryUpdateCommand command = new CategoryUpdateCommand(categoryId, "Updated Category", "#000000", "Updated Description", "http://example.com/updated-image.jpg");
        when(categoryRepository.findById(command.categoryId())).thenReturn(Optional.of(category));

        // When
        categoryService.update(command);

        // Then
        assertThat(category.getName()).isEqualTo("Updated Category");
        assertThat(category.getColor()).isEqualTo("#000000");
        assertThat(category.getDescription()).isEqualTo("Updated Description");
        assertThat(category.getImageUrl()).isEqualTo("http://example.com/updated-image.jpg");
    }

    @Test
    public void 카테고리_삭제_존재하지않는_카테고리_예외발생() {
        // Given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> categoryService.delete(categoryId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 카테고리입니다.");
    }

    @Test
    public void 카테고리_삭제_성공() {
        // Given
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        categoryService.delete(categoryId);

        // Then
        verify(productRepository, times(1)).deleteAllByCategoryId(categoryId);
        verify(categoryRepository, times(1)).delete(category);
    }
}
