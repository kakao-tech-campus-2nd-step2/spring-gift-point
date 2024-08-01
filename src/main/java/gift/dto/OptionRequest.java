package gift.dto;

import gift.model.Option;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class OptionRequest {

    @NotNull
    @Size(max = 50)
    @Pattern(regexp = "^[가-힣a-zA-Z0-9 ()\\[\\]\\+\\-&/_]*$")
    private String name;

    @Min(1)
    @Max(100000000)
    private Long quantity;

    public OptionRequest() {

    }

    public void DtoToEntity(){
        Option option = new Option();
        option.setOptionName(this.name);
        option.setQuantity(this.quantity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}
