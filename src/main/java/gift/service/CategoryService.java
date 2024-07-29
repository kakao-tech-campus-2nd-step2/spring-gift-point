package gift.service;

import static gift.controller.category.CategoryMapper.from;
import static gift.controller.category.CategoryMapper.toCategoryResponse;

import gift.controller.category.CategoryMapper;
import gift.controller.category.CategoryRequest;
import gift.controller.category.CategoryResponse;
import gift.domain.Category;
import gift.exception.CategoryAlreadyExistsException;
import gift.exception.CategoryNotExistsException;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> findAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<CategoryResponse> categoryResponses = categoryPage.stream()
            .map(CategoryMapper::toCategoryResponse).toList();
        return new PageImpl<>(categoryResponses, pageable, categoryPage.getTotalElements());
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryResponse(UUID id) {
        Category target = categoryRepository.findById(id)
            .orElseThrow(CategoryNotExistsException::new);
        return toCategoryResponse(target);
    }

    @Transactional(readOnly = true)
    public Category findByName(String categoryName) {
        return categoryRepository.findByName(categoryName)
            .orElseThrow(CategoryNotExistsException::new);
    }

    @Transactional
    public CategoryResponse save(CategoryRequest category) {
        categoryRepository.findByName(category.name()).ifPresent(p -> {
            throw new CategoryAlreadyExistsException();
        });
        return toCategoryResponse(categoryRepository.save(from(category)));
    }

    @Transactional
    public CategoryResponse update(UUID categoryId, CategoryRequest category) {
        Category target = categoryRepository.findById(categoryId)
            .orElseThrow(CategoryNotExistsException::new);
        target.updateDetails(category.name(), category.color(), category.description(),
            category.imageUrl());
        return toCategoryResponse(target);
    }

    @Transactional
    public void delete(UUID id) {
        categoryRepository.findById(id).orElseThrow(CategoryNotExistsException::new);
        categoryRepository.deleteById(id);
    }
}