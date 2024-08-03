package gift.dto.orderOption;

import gift.domain.CashReceipt;
import gift.domain.Member;
import gift.domain.Option;
import gift.domain.OrderOption;
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
