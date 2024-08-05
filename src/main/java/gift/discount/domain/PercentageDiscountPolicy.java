package gift.discount.domain;

public class PercentageDiscountPolicy implements DiscountPolicy {
    private final int percentage;
    private final int minimumOrderAmount;

    public PercentageDiscountPolicy(int percentage, int minimumOrderAmount) {
        this.percentage = percentage;
        this.minimumOrderAmount = minimumOrderAmount;
    }

    @Override
    public int calculateFinalAmount(int originalAmount) {
        if (isEligibleForDiscount(originalAmount)) {
            return originalAmount * (100 - percentage) / 100;
        }
        return originalAmount;
    }

    @Override
    public int calculateDiscountAmount(int originalAmount) {
        if (isEligibleForDiscount(originalAmount)) {
            return originalAmount * percentage / 100;
        }
        return 0;
    }

    @Override
    public boolean isEligibleForDiscount(int originalAmount) {
        return originalAmount >= minimumOrderAmount;
    }
}
