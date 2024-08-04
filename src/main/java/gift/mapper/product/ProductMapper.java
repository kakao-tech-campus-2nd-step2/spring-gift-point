package gift.mapper.product;

import gift.domain.product.Product.CreateProduct;
import gift.domain.product.Product.ProductDetail;
import gift.domain.product.Product.ProductSimple;
import gift.domain.product.Product.UpdateProduct;
import gift.entity.product.CategoryEntity;
import gift.entity.product.ProductEntity;
import gift.entity.product.ProductOptionEntity;
import gift.entity.product.WishEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Page<ProductSimple> toSimpleList(Page<ProductEntity> all) {
        List<ProductSimple> simpleList = all.stream()
            .map(entity -> new ProductSimple(
                entity.getId(),
                entity.getName()
            ))
            .collect(Collectors.toList());

        return new PageImpl<>(simpleList, all.getPageable(), all.getTotalElements());
    }

    public Page<ProductDetail> toDetailList(Page<ProductEntity> all) {
        List<ProductDetail> simpleList = all.stream()
            .map(entity -> new ProductDetail(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImageUrl(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getWish().stream().map(WishEntity::getId).collect(Collectors.toList()),
                entity.getCategory().getId(),
                entity.getOptions().stream().map(ProductOptionEntity::getId).collect(Collectors.toList())
            ))
            .collect(Collectors.toList());

        return new PageImpl<>(simpleList, all.getPageable(), all.getTotalElements());
    }

    public ProductDetail toDetail(ProductEntity entity) {
        return new ProductDetail(
            entity.getId(),
            entity.getName(),
            entity.getPrice(),
            entity.getImageUrl(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getWish().stream().map(WishEntity::getId).collect(Collectors.toList()),
            entity.getCategory().getId(),
            entity.getOptions().stream().map(ProductOptionEntity::getId).collect(Collectors.toList()));
    }

    public ProductEntity toEntity(CreateProduct create, CategoryEntity category) {
        return new ProductEntity(create.getName(), create.getPrice(), create.getImageUrl(),
            category);
    }

    public void setOption(ProductEntity product, ProductOptionEntity option) {
        product.addOption(option);
    }

    public ProductEntity toUpdate(UpdateProduct update, ProductEntity entity,
        CategoryEntity category) {
        entity.setPrice(update.getPrice());
        entity.setName(update.getName());
        entity.setImageUrl(update.getImageUrl());
        entity.setCategory(category);
        return entity;
    }

}
