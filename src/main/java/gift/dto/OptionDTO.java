package gift.dto;

import gift.annotation.OptionName;
import gift.entity.Option;
import gift.entity.Product;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class OptionDTO {

    @OptionName(message = "이름 규칙을 준수해야 합니다.")
    private String name;

    @DecimalMax(value = "100000000", message = "상품의 개수는 1억개 이하여야 합니다.")
    @DecimalMin(value = "1", message = "상품의 개수는 1개 이상이어야 합니다.")
    private int quantity;


    public OptionDTO() {
    }



    public OptionDTO(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }



    public Option toEntity(Product product) {
        return new Option(name, quantity, product);
    }
}
