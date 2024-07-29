package gift.dto;

import gift.entity.Option;
import gift.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class OptionRequestDto {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$",message = "허용되지 않는 문자가 있습니다.")
    private String name;

    @NotNull
    @Min(1)
    @Max(100000000)
    private int quantity;

    public OptionRequestDto() {}

    public OptionRequestDto(String name, int quantity) {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Option toEntity(Product product) {
        return new Option(name, quantity, product);
    }

}
