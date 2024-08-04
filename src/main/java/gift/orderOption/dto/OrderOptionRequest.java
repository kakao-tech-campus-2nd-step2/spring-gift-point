package gift.orderOption.dto;

import gift.orderOption.entity.CashReceipt;
import gift.member.entity.Member;
import gift.product.entity.Option;
import gift.orderOption.entity.OrderOption;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;

public record OrderOptionRequest(
    @NotNull
    long productId,

    @NotNull
    long optionId,

    @NotNull
    @Range(min = 1, max = 99999999)
    int quantity,

    @NotNull
    boolean hasCashReceipt,

    String cashReceiptType,

    String cashReceiptNumber,

    @NotNull
    String message,

    @Positive
    int point
) {

    public OrderOption toOrderOption(Option option, Member member) {
        return new OrderOption(productId, option, quantity, message, point, member);
    }

    public CashReceipt toCashReceipt() {
        return new CashReceipt(cashReceiptType, cashReceiptNumber);
    }
}
