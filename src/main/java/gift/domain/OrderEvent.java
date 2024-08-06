package gift.domain;


public class OrderEvent {
    private final Long orderId;
    private final Member member;
    private final Long productId;

    public OrderEvent(Long orderId, Member member, Long productId) {
        this.orderId = orderId;
        this.member = member;
        this.productId = productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Member getMember() {
        return member;
    }

    public Long getProductId() {
        return productId;
    }
}
