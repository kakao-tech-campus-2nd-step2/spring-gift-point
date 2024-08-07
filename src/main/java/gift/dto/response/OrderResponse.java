package gift.dto.response;

public class OrderResponse {

    private Long optionId;

    public OrderResponse(Long optionId) {
        this.optionId = optionId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

}
