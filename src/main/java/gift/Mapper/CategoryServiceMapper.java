package gift.Mapper;

import gift.DTO.CategoryDTO;
import gift.DTO.ProductDTO;
import gift.Entity.CategoryEntity;
import gift.Entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryServiceMapper {

    public CategoryDTO convertToDTO(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO(categoryEntity.getId(), categoryEntity.getName());
        categoryDTO.setId(categoryEntity.getId());
        categoryDTO.setName(categoryEntity.getName());
        categoryDTO.setParentId(categoryEntity.getParent() != null ? categoryEntity.getParent().getId() : null);

        List<CategoryDTO> children = categoryEntity.getChildren().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        categoryDTO.setChildren(children);

        List<ProductDTO> products = categoryEntity.getProducts().stream()
                .map(this::convertToProductDTO)
                .collect(Collectors.toList());
        categoryDTO.setProducts(products);

        return categoryDTO;
    }

    public CategoryEntity convertToEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryDTO.getId());
        categoryEntity.setName(categoryDTO.getName());

        return categoryEntity;
    }

    private ProductDTO convertToProductDTO(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productEntity.getId());
        productDTO.setName(productEntity.getName());

        return productDTO;
    }
}
