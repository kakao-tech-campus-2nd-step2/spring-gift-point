package gift.mapper;

import gift.domain.ProductOrder.OrderDetail;
import gift.domain.ProductOrder.OrderSimple;
import gift.entity.ProductOptionEntity;
import gift.entity.ProductOrderEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class ProductOrderMapper {
    public Page<OrderSimple> toSimple(Page<ProductOrderEntity> all) {
        List<OrderSimple> simpleList = all.stream()
            .map(entity -> new OrderSimple(
                entity.getUserEntity().getId(),
                entity.getOptions().getId(),
                entity.getQuantity()
            ))
            .collect(Collectors.toList());
        return new PageImpl<>(simpleList, all.getPageable(), all.getTotalElements());
    }

    public OrderDetail toDetail(ProductOptionEntity entity) {
        return new OrderDetail(
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
}
