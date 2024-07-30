package gift.dto;

import jakarta.validation.constraints.NotNull;

public class OptionDTO {

    private Long id;

    @NotNull(message = "옵션 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "옵션 수량을 입력해주세요.")
    private Integer quantity;

    private Long productId;

    public OptionDTO() {}

    public OptionDTO(Long id, String name, Integer quantity, Long productId) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}