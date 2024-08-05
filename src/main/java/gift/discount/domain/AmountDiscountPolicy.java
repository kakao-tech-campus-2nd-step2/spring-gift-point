package gift.discount.domain;


public class AmountDiscountPolicy implements DiscountPolicy {
    private final int discountAmount;
    private final int minimumOrderAmount;

    public AmountDiscountPolicy(int discountAmount,
                               int minimumOrderAmount) {
        this.discountAmount = discountAmount;
        this.minimumOrderAmount = minimumOrderAmount;
    }

    @Override
    public int calculateFinalAmount(int originalAmount) {
        if (isEligibleForDiscount(originalAmount)) {
            return originalAmount - discountAmount;
        }
        return originalAmount;
    }

    @Override
    public int calculateDiscountAmount(int originalAmount) {
        if (isEligibleForDiscount(originalAmount)) {
            return discountAmount;
        }
        return 0;
    }

    @Override
    public boolean isEligibleForDiscount(int originalAmount) {
        return originalAmount >= minimumOrderAmount;
    }
}
