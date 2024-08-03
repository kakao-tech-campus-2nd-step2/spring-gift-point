package gift.dto;

import java.time.LocalDateTime;

public class OrderDto {

    private final Long id;
    private MemberDto memberDto;
    private final Long optionId;
    private final int quantity;
    private final LocalDateTime orderDateTime;
    private final String message;

    public OrderDto(Long id, MemberDto memberDto, Long optionId, int quantity,
        LocalDateTime orderDateTime, String message) {
        this.id = id;
        this.memberDto = memberDto;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public MemberDto getMemberDto() {
        return memberDto;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }
}
