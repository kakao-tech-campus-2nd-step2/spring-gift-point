package gift.dto;

import gift.domain.option.Option;

public class OptionResponseDto {

    private final Long id;
    private final String name;
    private final Integer quantity;
    private final Long productId;


    public OptionResponseDto(Option option) {
        this.id = option.getId();
        this.name = option.getName();
        this.quantity = option.getQuantity();
        this.productId = option.getProduct().getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
