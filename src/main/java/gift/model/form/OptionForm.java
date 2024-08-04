package gift.model.form;

import gift.model.dto.OptionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;


@Schema(description = "상품 옵션 입력 폼 객체")
public class OptionForm {

    @Schema(description = "옵션 ID(옵션 최초 추가 시 입력 불필요)", example = "1")
    private Long id;

    @Schema(description = "옵션명", example = "색상: 블랙")
    @NotBlank(message = "옵션명은 필수 입력입니다.")
    @Length(min = 1, max = 50, message = "옵션의 길이는 1 ~ 50자 사이입니다.")
    @Pattern(regexp = "[a-zA-Z0-9가-힣() +\\-&\\[\\]/_]*", message = "(),[],+,-,&,/,_ 를 제외한 특수문자는 사용이 불가합니다.")
    private String name;

    @Schema(description = "옵션 수량", example = "100")
    @NotNull(message = "수량은 필수 입력입니다.")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    @Max(value = 100_000_000, message = "수량은 1억 미만입니다.")
    private Long quantity;

    public OptionForm(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public OptionForm(OptionDTO optionDTO) {
        this.id = optionDTO.getId();
        this.name = optionDTO.getName();
        this.quantity = optionDTO.getQuantity();
    }

    public OptionForm() {
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }


    public OptionDTO toDTO() {
        return new OptionDTO(id, null, name, quantity);
    }
}
