package gift.mapper.product;

import gift.domain.product.Category.CreateCategory;
import gift.domain.product.Category.DetailCategory;
import gift.domain.product.Category.SimpleCategory;
import gift.domain.product.Category.UpdateCategory;
import gift.entity.product.CategoryEntity;
import gift.entity.product.ProductEntity;
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
                entity.getName(),
                entity.getImageURL(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getBackgroundColor()
            ))
            .collect(Collectors.toList());

        return new PageImpl<>(simpleList, all.getPageable(), all.getTotalElements());
    }

    public Page<DetailCategory> toDetailList(Page<CategoryEntity> all) {
        List<DetailCategory> simpleList = all.stream()
            .map(entity -> new DetailCategory(
                entity.getProductEntities()
                    .stream()
                    .map(ProductEntity::getId)
                    .collect(
                        Collectors.toList()),
                entity.getId(),
                entity.getName(),
                entity.getImageURL(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getBackgroundColor(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
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
            entity.getImageURL(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getBackgroundColor(),
            entity.getCreatedAt(),
            entity.getUpdatedAt());
    }

    public CategoryEntity toEntity(CreateCategory create) {
        return new CategoryEntity(create.getName(), create.getImageURL(), create.getTitle(),
            create.getDescription(), create.getBackgroundColor());
    }

    public CategoryEntity toUpdate(UpdateCategory update, CategoryEntity entity) {
        entity.setName(update.getName());
        entity.setImageURL(update.getImageURL());
        entity.setTitle(update.getTitle());
        entity.setDescription(update.getDescription());
        entity.setBackgroundColor(update.getBackgroundColor());
        return entity;
    }

}
