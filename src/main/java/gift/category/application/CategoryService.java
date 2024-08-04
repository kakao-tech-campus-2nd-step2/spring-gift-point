package gift.category.application;

import gift.category.application.command.CategoryCreateCommand;
import gift.category.application.command.CategoryUpdateCommand;
import gift.category.domain.Category;
import gift.category.domain.CategoryRepository;
import gift.exception.type.DuplicateException;
import gift.exception.type.NotFoundException;
import gift.product.domain.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<CategoryServiceResponse> findAll() {
        return categoryRepository.findAll()
                .stream().map(CategoryServiceResponse::from).toList();
    }

    @Transactional
    public Long create(CategoryCreateCommand command) {
        categoryRepository.findByName(command.name())
                .ifPresent(category -> {
                    throw new DuplicateException("이미 존재하는 카테고리 이름 입니다.");
                });

        return categoryRepository.save(command.toCategory()).getId();
    }

    public CategoryServiceResponse findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(CategoryServiceResponse::from)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 카테고리입니다."));
    }

    @Transactional
    public void update(CategoryUpdateCommand command) {
        Category category = categoryRepository.findById(command.categoryId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 카테고리입니다."));

        category.update(command);
    }

    @Transactional
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 카테고리입니다."));

        productRepository.deleteAllByCategoryId(categoryId);
        categoryRepository.delete(category);
    }
}
