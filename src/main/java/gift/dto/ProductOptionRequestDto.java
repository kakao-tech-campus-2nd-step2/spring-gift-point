package gift.dto;

import gift.entity.OptionName;

public class ProductOptionRequestDto {
    private OptionName optionName;
    private int quantity;

    public ProductOptionRequestDto() {
    }

    public ProductOptionRequestDto(OptionName optionName, int quantity) {
        this.optionName = optionName;
        this.quantity = quantity;
    }

    public OptionName getOptionName() {
        return optionName;
    }

    public int getQuantity() {
        return quantity;
    }
}
