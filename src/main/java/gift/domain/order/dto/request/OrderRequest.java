package gift.domain.order.dto.request;

import gift.domain.member.Member;
import gift.domain.option.Option;
import gift.domain.order.Order;
import gift.domain.order.ReceiptType;
import gift.domain.product.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

public record OrderRequest(

    @NotNull
    Long productId,
    @NotNull
    Long optionId,
    @NotNull
    @Min(1)
    Long quantity,
    Boolean hasCashReceipt,
    @NotNull
    ReceiptType cashReceiptType,
    @NotNull
    String cashReceiptNumber,
    @NotBlank
    String message
) {

    public Order toOrder(Member member, Product product, Option option) {
        return new Order(
            member,
            product,
            option.getName(),
            this.quantity,
            LocalDateTime.now(),
            product.getPrice() * this.quantity.intValue(),
            product.getImageUrl(),
            this.hasCashReceipt,
            this.cashReceiptType,
            this.cashReceiptNumber,
            this.message
        );
    }
}
