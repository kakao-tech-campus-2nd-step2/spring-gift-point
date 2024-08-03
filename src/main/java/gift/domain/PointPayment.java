package gift.domain;

import gift.entity.DiscountPolicyEntity;
import gift.util.page.PageParam;
import java.time.LocalDateTime;

public class PointPayment {

    public PointPayment() {
    }

    public static class getList extends PageParam {

    }

    public static class CreatePointPayment{
        private Long userId;
        private Long productOptionId;
        private Long discountPolicyId;
        private Integer regularPrice;

        public CreatePointPayment(Long userId, Long productOptionId, Long discountPolicyId,
            Integer regularPrice) {
            this.userId = userId;
            this.productOptionId = productOptionId;
            this.discountPolicyId = discountPolicyId;
            this.regularPrice = regularPrice;
        }

        public Long getUserId() {
            return userId;
        }

        public Long getProductOptionId() {
            return productOptionId;
        }

        public Long getDiscountPolicyId() {
            return discountPolicyId;
        }

        public Integer getRegularPrice() {
            return regularPrice;
        }
    }

    public static class PointPaymentDetail{
        private Long id;
        private Long userId;
        private Long productOptionId;
        private Integer paymentAmount;
        private Integer regularPrice;
        private LocalDateTime transactionDate;
        private DiscountPolicyEntity discountPolicy;

        public PointPaymentDetail(Long id, Long userId, Long productOptionId,
            Integer paymentAmount, Integer regularPrice, LocalDateTime transactionDate,
            DiscountPolicyEntity discountPolicy) {
            this.id = id;
            this.userId = userId;
            this.productOptionId = productOptionId;
            this.paymentAmount = paymentAmount;
            this.regularPrice = regularPrice;
            this.transactionDate = transactionDate;
            this.discountPolicy = discountPolicy;
        }

        public Long getId() {
            return id;
        }

        public Long getUserId() {
            return userId;
        }

        public Long getProductOptionId() {
            return productOptionId;
        }

        public Integer getPaymentAmount() {
            return paymentAmount;
        }

        public Integer getRegularPrice() {
            return regularPrice;
        }

        public LocalDateTime getTransactionDate() {
            return transactionDate;
        }

        public DiscountPolicyEntity getDiscountPolicy() {
            return discountPolicy;
        }
    }

    public static class PointPaymentSimple{
        private Long id;
        private Long userId;
        private Long productOptionId;
        private Integer paymentAmount;

        public PointPaymentSimple(Long id, Long userId, Long productOptionId,
            Integer paymentAmount) {
            this.id = id;
            this.userId = userId;
            this.productOptionId = productOptionId;
            this.paymentAmount = paymentAmount;
        }

        public Long getId() {
            return id;
        }

        public Long getUserId() {
            return userId;
        }

        public Long getProductOptionId() {
            return productOptionId;
        }

        public Integer getPaymentAmount() {
            return paymentAmount;
        }
    }
}
