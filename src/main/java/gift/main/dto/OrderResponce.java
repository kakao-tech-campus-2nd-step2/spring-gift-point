package gift.main.dto;

import gift.main.entity.Order;

public record OrderResponce(
        String buyerName,
        String productName,
        String optionName,
        int quantity,
        String message,
        int payPrice,
        int usingPoint,
        int totalPrice

) {
    //만드는 주체는 누가 가지는게 좋을까??
    public OrderResponce(Order order) {
        this(
                order.getBuyer().getName(),
                order.getProduct().getName(),
                order.getOption().getOptionName(),
                order.getQuantity(),
                order.getMessage(),
                order.getPayPrice(),
                order.getUsingPoint(),
                order.getTotalPrice()
        );
    }

    @Override
    public String toString() {
        return "OrderResponce{" +
                "productName='" + productName + '\'' +
                ", optionName='" + optionName + '\'' +
                ", quantity=" + quantity +
                ", message='" + message + '\'' +
                '}';
    }
}
