package gift.service;

import gift.domain.Category.Category;
import gift.domain.Category.CategoryRequest;
import gift.domain.Menu.Menu;
import gift.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void create(CategoryRequest categoryRequest) {
        categoryRepository.save(mapCategoryRequsetToCategory(categoryRequest));
    }

    public void update(CategoryRequest categoryRequest) {
        categoryRepository.save(mapCategoryRequsetToCategory(categoryRequest));
    }

    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당하는 카테고리가 존재하지 않습니다."));
        List<Menu> menuList = category.getMenus();

        Category defaultCategory = categoryRepository.save(new Category("기본카테고리"));

        for (Menu menu : menuList) {
            menu.setCategory(defaultCategory);
        }
        categoryRepository.deleteById(id);
    }

    public List<Category> getCategotyList() {
        return categoryRepository.findAll();
    }
    public Category mapCategoryRequsetToCategory(CategoryRequest categoryRequest) {
        return new Category(null, categoryRequest.name(), new LinkedList<Menu>());
    }
}
