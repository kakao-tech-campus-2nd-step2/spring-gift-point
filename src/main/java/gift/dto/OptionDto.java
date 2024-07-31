package gift.dto;

import gift.entity.Option;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionDto {
    
    private Long id;

    @Size(max = 50, message = "Option name is too long!")
    @Pattern(regexp = "^[a-zA-Z0-9 ()\\[\\]+\\-\\&\\/\\_가-힣]*$", message = "Option name has invalid character")
    private String name;

    @Min(value = 1, message = "quantity must be more than 1")
    @Max(value = 99999999, message = "quantity must be less than 100,000,000")
    private int stockQuantity;

    public OptionDto(Long id, String name, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() {
        return id;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getName() {
        return name;
    }

    public static OptionDto fromEntity(Option option){
        return new OptionDto(option.getId(), option.getName(), option.getStockQuantity());
    }
}
