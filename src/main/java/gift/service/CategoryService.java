package gift.service;

import gift.dto.categoryDTO.CategoryRequestDTO;
import gift.dto.categoryDTO.CategoryResponseDTO;
import gift.exception.InvalidInputValueException;
import gift.exception.NotFoundException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public CategoryService(CategoryRepository categoryRepository,
        ProductRepository productRepository, WishlistRepository wishlistRepository,
        OptionRepository optionRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.wishlistRepository = wishlistRepository;
        this.optionRepository = optionRepository;
    }

    public List<CategoryResponseDTO> findAllCategories() {
        return categoryRepository.findAll().stream()
            .map(category -> toDTO(category))
            .toList();
    }

    public CategoryResponseDTO findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
        return toDTO(category);
    }

    public CategoryResponseDTO findCategoryByName(String name) {
        Category category = categoryRepository.findByName(name);
        return toDTO(category);
    }

    @Transactional
    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRepository.findByName(categoryRequestDTO.name()) != null) {
            throw new InvalidInputValueException("중복된 카테고리 이름입니다.");
        }
        Category category = requestToEntity(categoryRequestDTO, null);
        category = categoryRepository.save(category);
        return toDTO(category);
    }

    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
        existingCategory.updateCategory(categoryRequestDTO.name(), categoryRequestDTO.color(),
            categoryRequestDTO.imageUrl(), categoryRequestDTO.description());
        existingCategory = categoryRepository.save(existingCategory);
        return toDTO(existingCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
        List<Product> products = productRepository.findAllByCategoryId(id);
        if (!products.isEmpty()) {
            List<Long> productIds = products.stream().map(Product::getId).toList();
            List<Option> options = optionRepository.findAllByProductIdIn(productIds);
            wishlistRepository.deleteByOptionIn(options);
            optionRepository.deleteAllByProductIdIn(productIds);
            productRepository.deleteAll(products);
        }
        categoryRepository.delete(category);
    }

    public static CategoryResponseDTO toDTO(Category category) {
        return new CategoryResponseDTO(
            category.getId(),
            category.getName(),
            category.getColor(),
            category.getImageUrl(),
            category.getDescription()
        );
    }

    public Category requestToEntity(CategoryRequestDTO categoryRequestDTO, Long id) {
        return new Category(id, categoryRequestDTO.name(), categoryRequestDTO.color(),
            categoryRequestDTO.imageUrl(), categoryRequestDTO.description());
    }

    public Category responseToEntity(CategoryResponseDTO categoryResponseDTO) {
        return new Category(categoryResponseDTO.id(), categoryResponseDTO.name(),
            categoryResponseDTO.color(), categoryResponseDTO.imageUrl(),
            categoryResponseDTO.description());
    }

}
