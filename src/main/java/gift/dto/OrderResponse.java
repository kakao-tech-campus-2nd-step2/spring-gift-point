package gift.dto;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;

public class OrderResponse {
    private Long id;
    private Long optionId;
    private int quantity;
    private LocalDate orderDateTime;
    private String message;
    private HttpSession session;

    public OrderResponse(Long id, Long optionId, int quantity, LocalDate orderDateTime, String message, HttpSession session) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.session = session;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public HttpSession getSession() { // 세션 getter 추가
        return session;
    }
}
