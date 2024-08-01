package gift.dto;

import gift.domain.Order;
import gift.domain.member.Member;
import gift.dto.request.OrderCreateRequest;
import gift.dto.response.OrderResponse;

import java.time.LocalDateTime;

public class OrderDto {

    private Long id;
    private Member member;
    private Long optionId;
    private Long quantity;
    private String message;
    private LocalDateTime orderDateTime;
    private Integer point;

    public OrderDto(Member member, Long optionId, Long quantity, String message, Integer point) {
        this.member = member;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
        this.point = point;
    }

    public OrderDto(Long id, Long optionId, Long quantity, LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public OrderDto(Long id, Long optionId, Long quantity, Member member, String message, Integer point) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.member = member;
        this.message = message;
        this.point = point;
    }

    public static OrderDto of(Member member, OrderCreateRequest request) {
        return new OrderDto(member, request.getOptionId(), request.getQuantity(), request.getMessage(), request.getPoint());
    }

    public static OrderDto from(Order order) {
        return new OrderDto(order.getId(), order.getOption().getId(), order.getQuantity(), order.getOrderDateTime(), order.getMessage());
    }

    public Member getMember() {
        return member;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public Integer getPoint() {
        return point;
    }

    public OrderResponse toResponseDto() {
        return new OrderResponse(this.id, this.optionId, this.quantity, this.orderDateTime, this.message);
    }

}
