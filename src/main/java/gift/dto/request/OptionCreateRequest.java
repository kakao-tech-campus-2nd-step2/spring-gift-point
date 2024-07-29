package gift.dto.request;

import gift.dto.OptionDto;
import jakarta.validation.constraints.*;

public class OptionCreateRequest {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$", message = "옵션 이름에 허용되지 않는 특수 문자가 포함되어 있습니다.")
    private String name;

    @Min(1)
    @Max(99999999)
    @NotNull
    private Long quantity;

    public OptionCreateRequest() {
    }

    public OptionCreateRequest(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public OptionDto toDto() {
        return new OptionDto(this.name, this.quantity);
    }

}
