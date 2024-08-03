package gift.mapper.point;

import gift.domain.point.PointPayment.PointPaymentDetail;
import gift.domain.point.PointPayment.PointPaymentSimple;
import gift.entity.point.DiscountPolicyEntity;
import gift.entity.point.PointPaymentEntity;
import gift.entity.product.ProductOptionEntity;
import gift.entity.auth.UserEntity;
import gift.entity.enums.DiscountType;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

@Component
public class PointPaymentMapper {

    public Page<PointPaymentSimple> toSimpleList(Page<PointPaymentEntity> all) {
        List<PointPaymentSimple> simpleList = all.stream()
            .map(entity -> new PointPaymentSimple(
                entity.getId(),
                entity.getUser().getId(),
                entity.getProductOption().getId(),
                entity.getPaymentAmount()
            ))
            .collect(Collectors.toList());
        return new PageImpl<>(simpleList, all.getPageable(), all.getTotalElements());
    }

    public PointPaymentDetail toDetail(PointPaymentEntity entity) {
        PointPaymentDetail pointPaymentDetail
            = new PointPaymentDetail(
            entity.getId(),
            entity.getUser().getId(),
            entity.getProductOption().getId(),
            entity.getPaymentAmount(),
            entity.getRegularPrice(),
            entity.getTransactionDate(),
            entity.getDiscountPolicy().getId()
        );
        return pointPaymentDetail;
    }

    public PointPaymentEntity toEntity(Integer regularPrice, Integer paymentAmount,
        ProductOptionEntity option,
        UserEntity user, DiscountPolicyEntity discountPolicy) {
        return new PointPaymentEntity(regularPrice, paymentAmount, option, user, discountPolicy);
    }

    public Integer toDiscount(Integer regularPrice, DiscountPolicyEntity discountPolicy) {
        Integer paymentAmount = regularPrice;
        if (discountPolicy.getDiscountType().equals(DiscountType.FIX)) {
            if (discountPolicy.getDiscountAmountLimit() < discountPolicy.getDiscount()) {
                paymentAmount -= discountPolicy.getDiscountAmountLimit();
            } else {
                paymentAmount -= discountPolicy.getDiscount();
            }
        } else if (discountPolicy.getDiscountType().equals(DiscountType.PERCENT)) {
            Integer discount = (Integer) (regularPrice * discountPolicy.getDiscount() / 100);
            if (discountPolicy.getDiscountAmountLimit() < discount) {
                paymentAmount -= discountPolicy.getDiscountAmountLimit();
            } else {
                paymentAmount -= discount;
            }
        }

        return paymentAmount;
    }
}
