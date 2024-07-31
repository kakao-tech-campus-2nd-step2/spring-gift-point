package gift.service;

import gift.dto.CategoryRequestDto;
import gift.dto.CategoryResponseDto;
import gift.entity.Category;
import gift.exception.ServiceException;
import gift.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
            .map(CategoryResponseDto::new)
            .collect(Collectors.toList());
    }

    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("아이디 " + id + "에 해당 하는 상품을 찾을 수 없습니다."));

        return new CategoryResponseDto(category);
    }

    public void addCategory(CategoryRequestDto request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new ServiceException("중복된 카테고리 이름입니다.", HttpStatus.CONFLICT);
        }
        Category category = new Category(request.getName(), request.getImageUrl(), request.getColor(), request.getDescription());
        categoryRepository.save(category);
    }

    public void updateCategory(Long id, CategoryRequestDto request) {
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new ServiceException("존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND));

        // 변경하려는 카테고리 이름이 달라지고, 요청에 대한 카테고리명이 이미 존재한다면 에러 발생
        if (!existingCategory.getName().equals(request.getName()) && categoryRepository.existsByName(request.getName())) {
            throw new ServiceException("중복된 카테고리 이름입니다.", HttpStatus.CONFLICT);
        }

        Category newCategory = new Category(id, request.getName(), request.getImageUrl(),
            request.getColor(), request.getDescription());
        categoryRepository.save(newCategory);
    }

    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
            .orElseThrow(() -> new ServiceException("존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND));
        categoryRepository.deleteById(id);
    }
}