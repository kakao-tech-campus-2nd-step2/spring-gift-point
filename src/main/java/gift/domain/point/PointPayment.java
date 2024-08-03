package gift.domain.point;

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

        public CreatePointPayment(Long userId, Long productOptionId, Long discountPolicyId) {
            this.userId = userId;
            this.productOptionId = productOptionId;
            this.discountPolicyId = discountPolicyId;
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
    }

    public static class PointPaymentDetail{
        private Long id;
        private Long userId;
        private Long productOptionId;
        private Integer paymentAmount;
        private Integer regularPrice;
        private LocalDateTime transactionDate;
        private Long discountPolicyId;

        public PointPaymentDetail(Long id, Long userId, Long productOptionId,
            Integer paymentAmount, Integer regularPrice, LocalDateTime transactionDate,
            Long discountPolicyId) {
            this.id = id;
            this.userId = userId;
            this.productOptionId = productOptionId;
            this.paymentAmount = paymentAmount;
            this.regularPrice = regularPrice;
            this.transactionDate = transactionDate;
            this.discountPolicyId = discountPolicyId;
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

        public Long getDiscountPolicyId() {
            return discountPolicyId;
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
