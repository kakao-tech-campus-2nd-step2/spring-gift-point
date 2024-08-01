package gift.option.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "옵션 요청 DTO")
public class OptionRequestDTO {

    @Schema(description = "옵션 id")
    private long id;

    @Schema(description = "옵션명")
    private String name;

    @Schema(description = "옵션 수량")
    private int quantity;

    public OptionRequestDTO(long id, String name, int quantity) {
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
        if (obj instanceof OptionRequestDTO optionDTO) {
            return id == optionDTO.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }
}
