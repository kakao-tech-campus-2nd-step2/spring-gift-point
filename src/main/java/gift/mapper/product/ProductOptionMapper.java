package gift.mapper.product;

import gift.domain.product.ProductOption.CreateOption;
import gift.domain.product.ProductOption.UpdateOption;
import gift.domain.product.ProductOption.optionDetail;
import gift.domain.product.ProductOption.optionSimple;
import gift.entity.product.ProductEntity;
import gift.entity.product.ProductOptionEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class ProductOptionMapper {

    public Page<optionSimple> toSimple(Page<ProductOptionEntity> all) {
        List<optionSimple> simpleList = all.stream()
            .map(entity -> new optionSimple(
                entity.getProduct().getId(),
                entity.getId(),
                entity.getName(),
                entity.getQuantity()
            ))
            .collect(Collectors.toList());
        return new PageImpl<>(simpleList, all.getPageable(), all.getTotalElements());
    }

    public optionDetail toDetail(ProductOptionEntity entity) {
        return new optionDetail(
            entity.getProduct().getId(),
            entity.getProduct().getName(),
            entity.getProduct().getPrice(),
            entity.getId(),
            entity.getName(),
            entity.getQuantity(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public ProductOptionEntity toEntity(CreateOption create,
        ProductEntity productEntity) {
        return new ProductOptionEntity(create.getName(), create.getQuantity(), productEntity);
    }

    public ProductOptionEntity toUpdate(ProductOptionEntity entity, UpdateOption update) {
        entity.setName(update.getName());
        entity.setQuantity(update.getQuantity());
        return entity;
    }
}
