package gift.option.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "옵션 응답 DTO")
public class OptionResponseDTO {

    @Schema(description = "옵션 id")
    private long id;

    @Schema(description = "옵션명")
    private String name;

    @Schema(description = "남은 옵션 수량")
    private long quantity;

    public OptionResponseDTO(long id, String name, long quantity) {
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

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof OptionResponseDTO optionResponseDTO) {
            return id == optionResponseDTO.id
                   && quantity == optionResponseDTO.quantity
                   && Objects.equals(name, optionResponseDTO.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity);
    }
}
