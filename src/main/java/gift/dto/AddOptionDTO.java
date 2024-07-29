package gift.dto;

public class AddOptionDTO {
    private String optionName;
    private Integer quantity;

    public AddOptionDTO(String optionName, Integer quantity) {
        this.optionName = optionName;
        this.quantity = quantity;
    }

    public String getOptionName() {
        return optionName;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
