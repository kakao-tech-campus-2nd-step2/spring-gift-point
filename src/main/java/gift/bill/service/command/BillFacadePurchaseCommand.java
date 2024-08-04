package gift.bill.service.command;

public record BillFacadePurchaseCommand(
        Long optionId,
        Long memberId,
        Integer quantity,
        String message,
        Boolean usePoint,
        Integer pointValue
) {
    public BillCommand toBillCommand() {
        return new BillCommand(memberId, optionId, quantity, usePoint, pointValue);
    }
}
