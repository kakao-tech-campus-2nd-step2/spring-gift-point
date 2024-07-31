package gift.dto;

public class SendMessageRequest {

    private final String bearerToken;
    private final OrderRequest orderRequest;
    private final String accessToken;

    public SendMessageRequest(String bearerToken, OrderRequest orderRequest) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Bearer token");
        }
        this.bearerToken = bearerToken;
        this.orderRequest = orderRequest;
        this.accessToken = bearerToken.substring(7); // "Bearer " 이후의 실제 토큰 값
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public OrderRequest getOrderRequest() {
        return orderRequest;
    }

    public String getAccessToken() {
        return accessToken;
    }
}