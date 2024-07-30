package gift.service;

import gift.common.exception.DuplicateDataException;
import gift.common.exception.EntityNotFoundException;
import gift.controller.dto.request.CategoryRequest;
import gift.controller.dto.response.CategoryResponse;
import gift.controller.dto.response.PagingResponse;
import gift.model.Category;
import gift.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Long save(CategoryRequest.CreateCategory request) {
        checkDuplicateCategory(request);
        Category category = new Category(request.name(), request.color(), request.imageUrl(), request.description());
        return categoryRepository.save(category).getId();
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return CategoryResponse.from(categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with id " + id + " not found")));
    }

    @Transactional
    public void updateById(CategoryRequest.UpdateCategory request) {
        Category category = categoryRepository.findById(request.id())
                .orElseThrow(() ->
                        new EntityNotFoundException("Category with id " + request.id() + " not found"));
        category.updateCategory(request.name(), request.color(), request.imageUrl(), request.description());
    }

    @Transactional(readOnly = true)
    public PagingResponse<CategoryResponse> findAllPaging(Pageable pageable) {
        Page<CategoryResponse> pages = categoryRepository.findAll(pageable)
                .map(CategoryResponse::from);
        return PagingResponse.from(pages);
    }

    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    private void checkDuplicateCategory(CategoryRequest.CreateCategory request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateDataException("Category with name " + request.name() + " already exists", "Duplicate Category");
        }
    }
}
