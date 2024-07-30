package gift.service;

import gift.constants.ErrorMessage;
import gift.dto.CategoryDto;
import gift.entity.Category;
import gift.repository.CategoryJpaDao;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryJpaDao categoryJpaDao;

    public CategoryService(CategoryJpaDao categoryJpaDao) {
        this.categoryJpaDao = categoryJpaDao;
    }

    /**
     * Pageable 객체를 통해 해당 페이지 객체를 반환.
     *
     * @param pageable
     * @return Page
     */
    public Page<CategoryDto> getCategoryPage(Pageable pageable) {
        return categoryJpaDao.findAll(pageable).map(CategoryDto::new);
    }

    /**
     * 카테고리 전체 목록을 리스트로 반환.
     *
     * @return List
     */
    public List<CategoryDto> getCategoryList() {
        return categoryJpaDao.findAll().stream().map(CategoryDto::new).toList();
    }

    public CategoryDto getCategory(Long id) {
        Category category = findCategoryById(id);
        return new CategoryDto(category);
    }

    public void addCategory(CategoryDto categoryDto) {
        assertCategoryNotDuplicate(categoryDto.getName());
        categoryJpaDao.save(new Category(categoryDto));
    }

    @Transactional
    public void editCategory(CategoryDto categoryDto) {
        Category category = findCategoryById(categoryDto.getId());
        category.updateCategory(categoryDto);
    }

    public void deleteCategory(Long id) {
        findCategoryById(id);
        categoryJpaDao.deleteById(id);
    }

    /**
     * 카테고리 id로 존재하는 카테고리인지 확인하고, 존재하면 객체 반환.
     *
     * @param id
     * @return Category 객체
     */
    private Category findCategoryById(Long id) {
        return categoryJpaDao.findById(id)
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.CATEGORY_NOT_EXISTS_MSG));
    }

    /**
     * 카테고리 이름으로 카테고리 중복 여부 확인.
     *
     * @param name
     */
    private void assertCategoryNotDuplicate(String name) {
        categoryJpaDao.findByName(name)
            .ifPresent(v -> {
                throw new IllegalArgumentException(ErrorMessage.CATEGORY_ALREADY_EXISTS_MSG);
            });
    }
}
