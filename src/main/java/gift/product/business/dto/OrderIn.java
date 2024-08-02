package gift.product.business.dto;

import gift.member.persistence.entity.Member;
import gift.product.persistence.entity.Order;
import gift.product.persistence.entity.Product;

public class OrderIn {

    public record Create(
        Long memberId,
        Long productId,
        Long optionId,
        Integer quantity,
        Boolean hasCashReceipt,
        String cashReceiptType,
        String cashReceiptNumber,
        String message
    ) {

        public Order toOrder(Product product, Member member) {
            return new Order(
                product,
                member,
                optionId,
                quantity,
                hasCashReceipt,
                cashReceiptType,
                cashReceiptNumber,
                message
            );
        }
    }

}
