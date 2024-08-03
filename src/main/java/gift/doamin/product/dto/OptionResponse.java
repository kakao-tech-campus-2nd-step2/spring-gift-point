package gift.doamin.product.dto;

import gift.doamin.product.entity.Option;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "옵션 정보")
public class OptionResponse {

    @Schema(description = "옵션 id")
    private Long id;

    @Schema(description = "옵션명")
    private String name;

    @Schema(description = "옵션 수량")
    private int quantity;

    public OptionResponse(Option option) {
        this.id = option.getId();
        this.name = option.getName();
        this.quantity = option.getQuantity();
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
