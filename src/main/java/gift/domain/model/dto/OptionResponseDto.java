package gift.domain.model.dto;

import gift.domain.model.entity.Option;

public class OptionResponseDto {

    private final Long id;
    private final String name;
    private final int quantity;
    private final Long productId;
    private final String productName;

    public OptionResponseDto(Long id, String name, int quantity, Long productId,
        String productName) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.productId = productId;
        this.productName = productName;
    }

    public static OptionResponseDto toDto(Option option) {
        return new OptionResponseDto(option.getId(), option.getName(), option.getQuantity(),
            option.getProduct().getId(), option.getProduct().getName());
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

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }
}
