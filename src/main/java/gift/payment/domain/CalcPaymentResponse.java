package gift.payment.domain;

public class CalcPaymentResponse {
    private Long price;

    public CalcPaymentResponse(Long price) {
        this.price = price;
    }

    public Long getPrice() {
        return price;
    }
}
