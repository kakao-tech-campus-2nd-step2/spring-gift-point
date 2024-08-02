package gift.dto;

import gift.entity.Option;
import jakarta.validation.constraints.*;

public class OptionDto {

    private Long id;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$", message = "Invalid characters in option name")
    private String name;

    @Min(1)
    @Max(999999999)
    private int quantity;

    public OptionDto() {
    }

    public OptionDto(Option option) {
        this.id = option.getId();
        this.name = option.getName();
        this.quantity = option.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
