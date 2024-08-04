package gift.discount.domain;

public interface DiscountPolicy {
    int calculateFinalAmount(int originalAmount);
    int calculateDiscountAmount(int originalAmount);
    boolean isEligibleForDiscount(int originalAmount);
}
