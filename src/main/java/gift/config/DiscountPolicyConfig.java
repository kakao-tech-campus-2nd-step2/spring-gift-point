package gift.config;

import gift.discount.domain.AmountDiscountPolicy;
import gift.discount.domain.DiscountPolicy;
import gift.discount.domain.PercentageDiscountPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscountPolicyConfig {

    @Value("${discount.amount}")
    private int discountAmount;

    @Value("${discount.percentage}")
    private int discountPercentage;

    @Value("${discount.minimumOrderAmount}")
    private int minimumOrderAmount;

    @Bean
    public DiscountPolicy amountDiscountPolicy() {
        return new AmountDiscountPolicy(discountAmount, minimumOrderAmount);
    }

    @Bean
    public DiscountPolicy percentageDiscountPolicy() {
        return new PercentageDiscountPolicy(discountPercentage, minimumOrderAmount);
    }
}
