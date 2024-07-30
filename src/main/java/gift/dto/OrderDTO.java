package gift.dto;

import java.time.LocalDateTime;

public class OrderDTO {

    private Long id;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;

    protected OrderDTO() {
    }

    private OrderDTO(OrderDTOBuilder builder) {
        this.id = builder.id;
        this.optionId = builder.optionId;
        this.quantity = builder.quantity;
        this.orderDateTime = builder.orderDateTime;
        this.message = builder.message;
    }

    public Long getId() {
        return id;
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

    public static class OrderDTOBuilder {
        private Long id;
        private Long optionId;
        private int quantity;
        private LocalDateTime orderDateTime;
        private String message;

        public OrderDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderDTOBuilder optionId(Long optionId) {
            this.optionId = optionId;
            return this;
        }

        public OrderDTOBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderDTOBuilder orderDateTime(LocalDateTime orderDateTime) {
            this.orderDateTime = orderDateTime;
            return this;
        }

        public OrderDTOBuilder message(String message) {
            this.message = message;
            return this;
        }

        public OrderDTO build() {
            return new OrderDTO(this);
        }
    }
}
