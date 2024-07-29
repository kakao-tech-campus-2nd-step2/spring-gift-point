package gift.dto;

import gift.model.Option;
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
    private int quantity;

    @Schema(description = "상품 ID", example = "2001")
    @NotNull(message = "상품 ID는 필수 값입니다.")
    private Long productId;

    public OptionDTO() {}

    public OptionDTO(Long id, String name, int quantity, Long productId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public static OptionDTO convertToDTO(Option option) {
        return new OptionDTO(
            option.getId(),
            option.getName(),
            option.getQuantity(),
            option.getProduct().getId()
        );
    }
}
