package gift.dto.option;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class OptionRequest {

    @NotBlank
    @Size(max = 50, message = "The option name must be less than 50 characters, including spaces.") // since @NotBlank means constraints greater than 0, doesn't need to check min value.
    @Pattern(regexp = "^[\\w\\s()\\[\\]+\\-&/_가-힣]*$", message = "Invalid characters in option name")
    private String name;

    @NotNull
    @Min(value = 1, message = "quantity must be greater than 1")
    @Max(value = 99_999_999, message = "quantity can be up to less than 100 million")
    private Long quantity;

    public OptionRequest(Long id, String name, long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

}
