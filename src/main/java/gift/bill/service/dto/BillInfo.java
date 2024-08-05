package gift.bill.service.dto;

import gift.bill.domain.Bill;

public record BillInfo(
        Long id,
        Long memberId,
        Long productId,
        Long optionId,

        Integer quantity,
        Integer totalPrice,

        Boolean usePoint,
        Integer pointValue,
        Integer accumulatedPoint,

        Integer finalPrice
) {
    public static BillInfo from(Bill bill) {
        return new BillInfo(
                bill.getId(),
                bill.getMemberId(),
                bill.getProductId(),
                bill.getOptionId(),
                bill.getQuantity(),
                bill.getTotalPrice(),
                bill.getUsePoint(),
                bill.getPointValue(),
                bill.getAccumulatedPoint(),
                bill.getBilledPrice()
        );
    }

    public Integer discountPrice() {
        return totalPrice - finalPrice;
    }
}
