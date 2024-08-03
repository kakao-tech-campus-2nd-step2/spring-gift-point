package gift.init.point;

import gift.domain.point.DiscountPolicy.CreateDiscountPolicy;
import gift.entity.enums.DiscountType;
import gift.service.point.DiscountPolicyService;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscountPolicyCreator {

    private final DiscountPolicyService discountPolicyService;

    @Autowired
    public DiscountPolicyCreator(DiscountPolicyService discountPolicyService) {
        this.discountPolicyService = discountPolicyService;
    }

    public void creator() {
        discountPolicyService.createDiscountPolicy(new CreateDiscountPolicy(1L, DiscountType.FIX,100,
            LocalDateTime.parse("2024-09-20T15:23:01"),100,"fix test"));

        discountPolicyService.createDiscountPolicy(new CreateDiscountPolicy(1L, DiscountType.PERCENT,10,
            LocalDateTime.parse("2024-09-20T15:23:01"),10000,"percent test"));

    }
}
