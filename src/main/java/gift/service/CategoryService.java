package gift.service;

import gift.dto.CategoryRequestDto;
import gift.entity.Category;
import gift.exception.CategoryException;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category save(CategoryRequestDto categoryRequestDto) {
        if (categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new CategoryException("이미 존재하는 카테고리입니다..");
        }
        Category category = categoryRequestDto.toEntity();
        return categoryRepository.save(category);
    }

    public Category update(CategoryRequestDto categoryRequestDto, Long id) {
        Optional<Category> existCategory = categoryRepository.findById(id);
        if (existCategory.isEmpty()) {
            throw new CategoryException("카테고리를 찾을 수 없습니다.");
        }

        Category existingCategory = existCategory.get();

        if (!existingCategory.getName().equals(categoryRequestDto.getName()) &&
            categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new CategoryException("이미 존재하는 카테고리입니다.");
        }

        existingCategory.setName(categoryRequestDto.getName());
        existingCategory.setColor(categoryRequestDto.getColor());
        existingCategory.setImageUrl(categoryRequestDto.getImageUrl());
        existingCategory.setDescription(categoryRequestDto.getDescription());

        return categoryRepository.save(existingCategory);
    }

    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

}
