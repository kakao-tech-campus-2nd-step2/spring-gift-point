package gift.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "옵션 응답 DTO")
public class ResponseOptionDTO {
    @Schema(description = "옵션 Id")
    private Long id;

    @Schema(description = "옵션 이름")
    private String name;

    @Schema(description = "옵션 갯수")
    private int quantity;

    public ResponseOptionDTO(Long id, String name, int quantity) {
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
