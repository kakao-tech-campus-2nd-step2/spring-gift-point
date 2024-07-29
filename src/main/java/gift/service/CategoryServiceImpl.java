package gift.service;

import gift.dto.CategoryDTO;
import gift.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
            .map(CategoryDTO::convertDTO)
            .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(int id) {
        return categoryRepository.findById(id)
            .map(CategoryDTO::convertDTO)
            .orElse(null);
    }
}
