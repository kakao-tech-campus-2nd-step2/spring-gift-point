package gift.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class OptionDTO {

    private Long id;

    @Valid
    @NotNull(message = "옵션 이름을 입력해주세요.")
    private OptionNameDTO name;

    @Valid
    @NotNull(message = "옵션 수량을 입력해주세요.")
    private OptionQuantityDTO quantity;

    private Long productId;

    public OptionDTO() {}

    public OptionDTO(Long id, OptionNameDTO name, OptionQuantityDTO quantity, Long productId) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OptionNameDTO getName() {
        return name;
    }

    public void setName(OptionNameDTO name) {
        this.name = name;
    }

    public OptionQuantityDTO getQuantity() {
        return quantity;
    }

    public void setQuantity(OptionQuantityDTO quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}