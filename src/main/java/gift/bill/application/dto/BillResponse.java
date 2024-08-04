package gift.bill.application.dto;

import gift.bill.service.dto.BillInfo;

public record BillResponse(
        Long orderId,
        Integer totalPrice,
        Integer discountPrice,
        Integer accumulatedPoint
) {
    public static BillResponse from(BillInfo billInfo) {
        return new BillResponse(
                billInfo.id(),
                billInfo.totalPrice(),
                billInfo.discountPrice(),
                billInfo.accumulatedPoint()
        );
    }
}
