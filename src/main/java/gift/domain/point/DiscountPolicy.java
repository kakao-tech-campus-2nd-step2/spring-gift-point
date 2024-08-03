package gift.domain.point;

import gift.entity.enums.DiscountType;
import gift.util.page.PageParam;
import java.time.LocalDateTime;
import java.util.List;

public class DiscountPolicy {

    public DiscountPolicy() {
    }
    public static class getList extends PageParam {

    }
    public static class CreateDiscountPolicy{
        private Long productId;
        private DiscountType discountType;
        private Integer discount;
        private LocalDateTime endDate;
        private Integer discountAmountLimit;
        private String remark;

        public CreateDiscountPolicy(Long productId, DiscountType discountType, Integer discount,
            LocalDateTime endDate, Integer discountAmountLimit, String remark) {
            this.productId = productId;
            this.discountType = discountType;
            this.discount = discount;
            this.endDate = endDate;
            this.discountAmountLimit = discountAmountLimit;
            this.remark = remark;
        }

        public Long getProductId() {
            return productId;
        }

        public DiscountType getDiscountType() {
            return discountType;
        }

        public Integer getDiscount() {
            return discount;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public Integer getDiscountAmountLimit() {
            return discountAmountLimit;
        }

        public String getRemark() {
            return remark;
        }
    }
    public static class DiscountPolicySimple{
        private Long id;
        private DiscountType discountType;
        private Integer discount;
        private Long targetProductId;

        public DiscountPolicySimple(Long id, DiscountType discountType, Integer discount,
            Long targetProductId) {
            this.id = id;
            this.discountType = discountType;
            this.discount = discount;
            this.targetProductId = targetProductId;
        }

        public Long getId() {
            return id;
        }

        public DiscountType getDiscountType() {
            return discountType;
        }

        public Integer getDiscount() {
            return discount;
        }

        public Long getTargetProductId() {
            return targetProductId;
        }
    }
    public static class DiscountPolicyDetail{
        private Long id;
        private DiscountType discountType;
        private Integer discount;
        private LocalDateTime createdAt;
        private LocalDateTime endDate;
        private String remark;
        private Long targetProductId;
        private List<Long> PointPayment;

        public DiscountPolicyDetail(Long id, DiscountType discountType, Integer discount,
            LocalDateTime createdAt, LocalDateTime endDate, String remark, Long targetProductId,
            List<Long> pointPayment) {
            this.id = id;
            this.discountType = discountType;
            this.discount = discount;
            this.createdAt = createdAt;
            this.endDate = endDate;
            this.remark = remark;
            this.targetProductId = targetProductId;
            PointPayment = pointPayment;
        }

        public Long getId() {
            return id;
        }

        public DiscountType getDiscountType() {
            return discountType;
        }

        public Integer getDiscount() {
            return discount;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public LocalDateTime getEndDate() {
            return endDate;
        }

        public String getRemark() {
            return remark;
        }

        public Long getTargetProductId() {
            return targetProductId;
        }

        public List<Long> getPointPayment() {
            return PointPayment;
        }
    }
}
