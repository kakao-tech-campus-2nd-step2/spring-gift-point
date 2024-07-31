package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {

    @JsonProperty("option_id")
    private Long optionId;
    private int quantity;
    private String message;

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

}
