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
    public CategoryResponse.InfoList getAllCategories() {
        return CategoryResponse.InfoList.from(categoryRepository.findAll());
    }

    @Transactional(readOnly = true)
    public CategoryResponse.Info findById(Long id) {
        return CategoryResponse.Info.from(categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 카테고리입니다.")));
    }

    @Transactional
    public void updateById(CategoryRequest.UpdateCategory request) {
        Category category = categoryRepository.findById(request.id())
                .orElseThrow(() ->
                        new EntityNotFoundException("존재하지 않는 카테고리입니다."));
        category.updateCategory(request.name(), request.color(), request.imageUrl(), request.description());
    }

    @Transactional(readOnly = true)
    public PagingResponse<CategoryResponse.Info> findAllPaging(Pageable pageable) {
        Page<CategoryResponse.Info> pages = categoryRepository.findAll(pageable)
                .map(CategoryResponse.Info::from);
        return PagingResponse.from(pages);
    }

    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    private void checkDuplicateCategory(CategoryRequest.CreateCategory request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateDataException("이미 존재하는 카테고리명입니다.", "Duplicate Category");
        }
    }
}
