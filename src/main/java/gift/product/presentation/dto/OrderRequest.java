package gift.product.presentation.dto;

import gift.product.business.dto.OrderIn;
import jakarta.validation.constraints.Min;

public class OrderRequest {

    public record Create(
        @Min(1)
        Long productId,
        @Min(1)
        Long optionId,
        @Min(1)
        Integer quantity,
        Boolean hasCashReceipt,
        String cashReceiptType,
        String cashReceiptNumber,
        String message
    ) {

        public OrderIn.Create toOrderInCreate(Long memberId) {
            return new OrderIn.Create(
                memberId,
                productId,
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
