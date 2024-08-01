package gift.dto;

import gift.model.Product;
import jakarta.validation.constraints.*;

public class OptionDTO {

    private Long id;

    @NotBlank(message = "옵션 이름은 필수입니다")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력 가능합니다")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣()\\[\\]+\\-&/_.\\s]*$", message = "사용 불가능한 특수 문자가 포함되어 있습니다")
    private String name;

    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다")
    @Max(value = 999999999, message = "수량은 1억개 미만이어야 합니다")
    private int quantity;

    public OptionDTO() {
    }

    public OptionDTO(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}