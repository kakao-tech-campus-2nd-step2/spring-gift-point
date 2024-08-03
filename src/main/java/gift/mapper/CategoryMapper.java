package gift.mapper;

import gift.DTO.CategoryDTO;
import gift.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryDTO toCategoryDTO(CategoryEntity categoryEntity) {
        return new CategoryDTO(
                categoryEntity.getId(),
                categoryEntity.getName(),
                categoryEntity.getDescription(),
                categoryEntity.getImageUrl(),
                categoryEntity.getColor()
                );
    }

    public CategoryEntity toCategoryEntity(CategoryDTO categoryDTO, boolean idRequired) {
        var categoryEntity = new CategoryEntity();
        if (idRequired) {
            categoryEntity.setId(categoryDTO.id());
        }
        categoryEntity.setName(categoryDTO.name());
        categoryEntity.setColor(categoryDTO.color());
        categoryEntity.setImageUrl(categoryDTO.imageUrl());
        categoryEntity.setDescription(categoryDTO.description());
        return categoryEntity;
    }

    public CategoryEntity toCategoryEntity(CategoryDTO categoryDTO) {
        return toCategoryEntity(categoryDTO, true);
    }
}
