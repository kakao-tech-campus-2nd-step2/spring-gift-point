package gift.mapper.point;

import gift.domain.point.DiscountPolicy.CreateDiscountPolicy;
import gift.domain.point.DiscountPolicy.DiscountPolicyDetail;
import gift.domain.point.DiscountPolicy.DiscountPolicySimple;
import gift.entity.point.DiscountPolicyEntity;
import gift.entity.point.PointPaymentEntity;
import gift.entity.product.ProductEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class DiscountPolicyMapper {

    public Page<DiscountPolicySimple> toSimpleList(Page<DiscountPolicyEntity> all) {
        List<DiscountPolicySimple> simpleList = all.stream()
            .map(entity -> new DiscountPolicySimple(
                entity.getId(),
                entity.getDiscountType(),
                entity.getDiscount(),
                entity.getTarget().getId()
            ))
            .collect(Collectors.toList());
        return new PageImpl<>(simpleList, all.getPageable(), all.getTotalElements());
    }

    public DiscountPolicyDetail toDetail(DiscountPolicyEntity entity) {
        DiscountPolicyDetail detail = new DiscountPolicyDetail(
            entity.getId(),
            entity.getDiscountType(),
            entity.getDiscount(),
            entity.getCreatedAt(),
            entity.getEndDate(),
            entity.getRemark(),
            entity.getTarget().getId(),
            entity.getPointPayment().stream().map(PointPaymentEntity::getId).collect(Collectors.toList())
        );

        return detail;
    }

    public DiscountPolicyEntity toEntity(CreateDiscountPolicy create, ProductEntity product) {
        DiscountPolicyEntity entity = new DiscountPolicyEntity(
            create.getDiscountType(),
            create.getDiscount(),
            create.getEndDate(),
            create.getDiscountAmountLimit(),
            create.getRemark(),
            product);

        return entity;
    }
}
