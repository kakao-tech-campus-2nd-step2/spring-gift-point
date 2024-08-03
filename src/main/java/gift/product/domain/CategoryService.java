package gift.product.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import gift.core.exception.product.CategoryNotFoundException;
import gift.product.repository.CategoryJpaRepository;

@Service
public class CategoryService {
	private final CategoryJpaRepository categoryRepository;

	public CategoryService(CategoryJpaRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public Page<Category> getAllCategories(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}
}
