package gift.bill.service.command;

public record BillCommand(
        Long memberId,
        Long optionId,
        Integer quantity,
        Boolean usePoint,
        Integer pointValue
) {
}