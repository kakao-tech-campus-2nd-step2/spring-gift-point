package gift.payment.application;

import org.springframework.stereotype.Service;

@Service
public class PaymentEventService {
    private static final Long DISCOUNT_PRICE = 50000L;
    private static final double DISCOUNT_RATE = 0.1;

    public Long 일정금액_이상이면_할인이벤트(Long price) {
        if (price >= DISCOUNT_PRICE) {
            return (long) (price * (1 - DISCOUNT_RATE));
        }
        return price;
    }
}
