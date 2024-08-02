package gift.DTO;

public class OrderResponseDTO {
    private Long orderId;
    private String status;
    private String message;

    public OrderResponseDTO(Long orderId, String status, String message) {
        this.orderId = orderId;
        this.status = status;
        this.message = message;
    }


}
