package gift.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionRequest {
    
    @Size(max = 50, message = "Option name is too long!")
    @Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-\\&\\/\\_가-힣]*$", message = "Option name has invalid character")
    private String optionName;

    @Min(value = 1, message = "quantity must be more than 1")
    @Max(value = 99999999, message = "quantity must be less than 100,000,000")
    private int quantity;

    public OptionRequest(String optionName, int quantity){
        this.optionName = optionName;
        this.quantity = quantity;
    }

    public String getOptionName(){
        return optionName;
    }

    public int getQuantity(){
        return quantity;
    }

}
