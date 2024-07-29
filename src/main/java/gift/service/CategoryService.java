package gift.service;

import gift.dto.betweenClient.category.CategoryDTO;
import gift.entity.Category;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.InternalServerExceptions.InternalServerException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    CategoryRepository categoryRepository;
    ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategories() {
        try {
            List<Category> categoryList = categoryRepository.findAll();
            return categoryList.stream().map(CategoryDTO::convertToCategoryDTO).toList();
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @Transactional
    public void addCategory(CategoryDTO categoryDTO) {
        try {
            if(categoryRepository.existsByName(categoryDTO.name()))
                throw new BadRequestException("중복된 카테고리 이름입니다.");

            Category category = categoryDTO.convertToCategory();
            categoryRepository.save(category);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @Transactional
    public void updateCategory(Long id, CategoryDTO categoryDTO) {
        try{
            if(categoryRepository.existsByName(categoryDTO.name()))
                throw new BadRequestException("이미 존재하는 카테고리 이름 입니다.");

            Category categoryInDb = categoryRepository.findById(id).orElseThrow(() -> new BadRequestException("그러한 id를 가지는 카테고리는 없습니다."));
            Category categoryToReplace = categoryDTO.convertToCategory();
            categoryInDb.changeCategory(categoryToReplace.getName(), categoryToReplace.getColor(), categoryToReplace.getImageUrl(), categoryToReplace.getDescription());
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @Transactional
    public void deleteCategory(Long id) {
        try {
            if(productRepository.existsByCategoryId(id))
                throw new BadRequestException("해당 카테고리 아이디를 가지는 상품이 있어서 제거할 수 없습니다.");
            categoryRepository.deleteById(id);
        } catch (BadRequestException e) {
            throw e;
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequestException("id가 %d인 카테고리를 찾을 수 없습니다.".formatted(id));
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}
