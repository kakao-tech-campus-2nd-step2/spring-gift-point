package gift.option;

import static gift.exception.ErrorMessage.OPTION_NAME_ALLOWED_CHARACTER;
import static gift.exception.ErrorMessage.OPTION_NAME_LENGTH;
import static gift.exception.ErrorMessage.OPTION_QUANTITY_SIZE;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Schema(description = "옵션 수신용 DTO")
public class OptionDTO {

    @Schema(description = "옵션 id")
    private long id;

    @Pattern(regexp = "^[a-zA-Z0-9가-힣\s()\\[\\]+\\-&/_]*$", message = OPTION_NAME_ALLOWED_CHARACTER)
    @Length(min = 1, max = 50, message = OPTION_NAME_LENGTH)
    @Schema(description = "옵션명")
    private String name;

    @Range(min = 1, max = 99999999, message = OPTION_QUANTITY_SIZE)
    @Schema(description = "옵션 수량")
    private int quantity;

    public OptionDTO(long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OptionDTO optionDTO) {
            return id == optionDTO.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }
}
