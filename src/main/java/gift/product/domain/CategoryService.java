package gift.product.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import gift.core.exception.product.CategoryNotFoundException;
import gift.product.repository.CategoryJpaRepository;

@Service
public class CategoryService {
	private final CategoryJpaRepository categoryRepository;

	public CategoryService(CategoryJpaRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}
}
