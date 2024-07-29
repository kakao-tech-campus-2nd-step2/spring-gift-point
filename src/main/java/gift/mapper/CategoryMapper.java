package gift.mapper;

import gift.domain.Category.DetailCategory;
import gift.domain.Category.SimpleCategory;
import gift.entity.CategoryEntity;
import gift.entity.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Page<SimpleCategory> toSimpleList(Page<CategoryEntity> all) {
        List<SimpleCategory> simpleList = all.stream()
            .map(entity -> new SimpleCategory(
                entity.getId(),
                entity.getName()
            ))
            .collect(Collectors.toList());

        return new PageImpl<>(simpleList, all.getPageable(), all.getTotalElements());
    }

    public DetailCategory toDetail(CategoryEntity entity) {
        return new DetailCategory(
            entity.getProductEntities()
                .stream()
                .map(ProductEntity::getId)
                .collect(
                    Collectors.toList()),
            entity.getId(),
            entity.getName(),
            entity.getCreatedAt(),
            entity.getUpdatedAt());
    }

    public CategoryEntity toEntity(String name) {
        return new CategoryEntity(name);
    }

    public CategoryEntity toUpdate(String name, CategoryEntity entity) {
        entity.setName(name);
        return entity;
    }

}
