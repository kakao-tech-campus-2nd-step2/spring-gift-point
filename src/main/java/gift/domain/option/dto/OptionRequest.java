package gift.domain.option.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "옵션 요청 DTO")
public class OptionRequest {

    @NotBlank
    @Size(max = 50, message = "최대 50자리까지 입력하실 수 있습니다.")
    @Pattern(regexp = "[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/가-힣]*", message = "특수 문자는 '(), [], +, -, &, /, _ '만 사용가능 합니다.")
    @Schema(description = "옵션 이름", example = "옵션1")
    private String name;

    @NotNull
    @Max(value = 99_999_999, message = "최대 수량은 1억개 미만으로 설정해주세요")
    @Min(value = 1, message = "최소 수량은 1개 이상으로 설정해주세요")
    @Schema(description = "옵션 수량", example = "10")
    private int quantity;

    private OptionRequest() {
    }

    public OptionRequest(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
