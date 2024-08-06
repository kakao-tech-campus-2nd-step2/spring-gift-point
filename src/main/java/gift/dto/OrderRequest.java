package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"option_id", "quantity", "point_amount", "message"})
public class OrderRequest {

    @JsonProperty("option_id")
    private Long optionId;
    private int quantity;

    @JsonProperty("point_amount")
    private Long pointAmount;
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

    public Long getPointAmount() {
        return pointAmount;
    }
}
