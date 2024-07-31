package gift.doamin.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "옵션 정보")
public class OptionParam {

    @Schema(description = "옵션 id")
    private Long id;

    @Schema(description = "옵션명")
    private String name;

    @Schema(description = "옵션 수량")
    private int quantity;

    public OptionParam(Long id, String name, int quantity) {
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
