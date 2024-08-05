package gift.dto.response;

public class OrderResponse {
    private Long id;
    private Long optionId;
    private int quantity;
    private String message;


    public OrderResponse(Long id, Long optionId, int quantity, String message){
        this.id =id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public void setId(Long id) {this.id = id;}

    public void setOptionId(Long optionId) {this.optionId = optionId;}

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public void setMessage(String message) {this.message = message;}

    public Long getId() {return id;}

    public Long getOptionId() {return optionId;}

    public int getQuantity() {return quantity;}

    public String getMessage() {return message;}
}
