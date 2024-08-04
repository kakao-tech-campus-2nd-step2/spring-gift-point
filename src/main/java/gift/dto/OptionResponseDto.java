package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "옵션 응답 DTO")
public class OptionResponseDto {
    @Schema(description = "옵션 고유 id")
    private final Long id;
    @Schema(description = "옵션 이름")
    private final String name;
    @Schema(description = "옵션 개수")
    private final Long quantity;
    @Schema(description = "옵션이 속한 상품 id")
    private final Long product_id;

    public OptionResponseDto(Long id, String name, Long quantity, Long product_id) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product_id = product_id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getProduct_id() {
        return product_id;
    }
}