package gift.doamin.product.dto;

import gift.doamin.product.entity.Option;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Schema(description = "옵션 추가, 삭제 요청")
public class OptionForm {

    @Schema(description = "옵션명")
    @Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣\\w ()\\[\\]+\\-&/_]{1,50}$", message = "이름 형식이 잘못되었습니다.")
    private String name;

    @Schema(description = "옵션 수량")
    @Positive
    @Max(99_999_999)
    private Integer quantity;

    public OptionForm(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Option toEntity() {
        return new Option(name, quantity);
    }
}
