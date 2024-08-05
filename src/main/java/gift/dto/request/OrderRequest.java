package gift.dto.request;

public class OrderRequest {
    
    private Long optionId;
    private int quantity;
    private String message;
    private int pointAmount;

    public Long getOptionId(){
        return optionId;
    }

    public int getQuantity(){
        return quantity;
    }

    public String getMessage(){
        return message;
    }

    public int getPointAmount(){
        return pointAmount;
    }

}
