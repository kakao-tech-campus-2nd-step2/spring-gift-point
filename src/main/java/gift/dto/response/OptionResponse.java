package gift.dto.response;

import gift.entity.Option;

public class OptionResponse {
    private Long optionId;
    private String optionName;
    private int quantity;



    public OptionResponse(Option option){
        this.optionId = option.getId();
        this.optionName = option.getName();
        this.quantity = option.getQuantity();
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getOptionId() {
        return optionId;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
