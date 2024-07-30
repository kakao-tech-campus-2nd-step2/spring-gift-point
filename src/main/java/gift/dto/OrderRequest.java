package gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderRequest {
    private Long optionId;
    private int quantity;
    private String message;

    @JsonCreator
    public OrderRequest(@JsonProperty("optionId") Long optionId,
                        @JsonProperty("quantity") int quantity,
                        @JsonProperty("message") String message) {
        if (optionId == null) {
            throw new IllegalArgumentException("Option id를 입력해야 합니다.");
        }
        this.optionId = optionId;
        this.quantity = quantity;
        this.message = message;
    }

    public static class Builder {
        private Long optionId;
        private int quantity;
        private String message;

        public Builder optionId(Long optionId) {
            this.optionId = optionId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public OrderRequest build() {
            return new OrderRequest(optionId, quantity, message);
        }
    }

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