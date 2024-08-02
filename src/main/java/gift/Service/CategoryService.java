package gift.Service;

import gift.Entity.Category;
import gift.Mapper.Mapper;
import gift.Model.CategoryDto;
import gift.Repository.CategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;
    private final Mapper mapper;

    @Autowired
    public CategoryService(CategoryJpaRepository categoryJpaRepository, Mapper mapper) {
        this.categoryJpaRepository = categoryJpaRepository;
        this.mapper = mapper;
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categoryList = categoryJpaRepository.findAll();
        return categoryList.stream().map(mapper::categoryToDto).collect(Collectors.toList());
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryJpaRepository.findById(id).get();
        return mapper.categoryToDto(category);
    }

    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = mapper.categoryDtoToEntity(categoryDto);
        Category addedCategory = categoryJpaRepository.save(category);
        return mapper.categoryToDto(addedCategory);
    }

    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = mapper.categoryDtoToEntity(categoryDto);
        return mapper.categoryToDto(category);
    }

}
