package gift.category.service;

import gift.category.dto.CategoryRequest;
import gift.category.dto.CategoryResponse;
import gift.category.entity.Category;
import gift.repository.JpaCategoryRepository;
import gift.exceptionAdvisor.exceptions.GiftNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CategoryService {

    private final JpaCategoryRepository jpaCategoryRepository;

    public CategoryService(JpaCategoryRepository jpaCategoryRepository) {
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    public List<CategoryResponse> readAll() {
        return jpaCategoryRepository.findAll().stream().map(CategoryResponse::new).toList();

    }

    public void create(CategoryRequest categoryRequest) {
        Category category = getCategory(null, categoryRequest);

        jpaCategoryRepository.save(category);
    }


    public void update(long id, CategoryRequest categoryRequest) {
        Category category = getCategory(id, categoryRequest);
        jpaCategoryRepository.save(category);
    }

    public void delete(long id) {
        jpaCategoryRepository.deleteById(id);
    }

    private static Category getCategory(Long id, CategoryRequest categoryRequest) {
        return new Category(id, categoryRequest.getName(), categoryRequest.getColor(),
            categoryRequest.getDescription(), categoryRequest.getImageUrl());
    }

    public CategoryResponse read(Long id) {
        Category category = jpaCategoryRepository.findById(id)
            .orElseThrow(() -> new GiftNotFoundException("카테고리가 존재하지 않습니다."));

        return new CategoryResponse(category);
    }
}
