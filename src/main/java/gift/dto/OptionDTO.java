package gift.dto;

import gift.model.Option;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "옵션 DTO")
public class OptionDTO {

    @Schema(description = "옵션 ID", example = "1")
    @NotNull(message = "ID는 필수 값입니다.")
    private Long id;

    @Schema(description = "옵션 이름", example = "포장 선택")
    @NotBlank(message = "이름은 필수 값입니다.")
    @Size(max = 50, message = "이름은 최대 50자까지 가능합니다.")
    private String name;

    @Schema(description = "옵션 수량", example = "1")
    @Min(value = 1, message = "수량은 최소 1 이상이어야 합니다.")
    @Max(value = 100000000, message = "수량은 최대 1억개 이하이어야 합니다.")
    private int quantity;

    @Schema(description = "옵션 가격", example = "3000")
    @Min(value = 0, message = "가격은 최소 0 이상이어야 합니다.")
    private int price;

    @Schema(description = "상품 ID", example = "2001")
    @NotNull(message = "상품 ID는 필수 값입니다.")
    private Long productId;

    @Schema(description = "최대 수량", example = "100")
    @Max(value = 100000000, message = "수량은 최대 1억개 이하이어야 합니다.")
    private int maxQuantity;

    public OptionDTO() {}

    public OptionDTO(Long id, String name, int quantity, int price, Long productId, int maxQuantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
        this.maxQuantity = maxQuantity;
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

    public int getPrice() {
        return price;
    }

    public Long getProductId() {
        return productId;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public static OptionDTO convertToDTO(Option option) {
        return new OptionDTO(
            option.getId(),
            option.getName(),
            option.getQuantity(),
            option.getPrice(),
            option.getProduct().getId(),
            option.getMaxQuantity()
        );
    }
}
