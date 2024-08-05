package gift.bill.application.dto;

import gift.bill.service.command.BillFacadePurchaseCommand;
import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(
        @NotNull
        Long optionId,
        @NotNull
        Integer quantity,
        @NotNull
        String message,
        @NotNull
        Boolean usePoint,
        @NotNull
        Integer point
) {
    public BillFacadePurchaseCommand toCommand(Long memberId) {
        return new BillFacadePurchaseCommand(optionId, memberId, quantity, message, usePoint, point);
    }
}
