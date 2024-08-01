package gift.dto;

import gift.entity.Option;

public class OptionResponse {

    private final Long id;
    private final String name;
    private final int quantity;
    private final Long productId;

    public OptionResponse(Option option, Long productId) {
        this.id = option.getId();
        this.name = option.getName();
        this.quantity = option.getQuantity();
        this.productId = productId;
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
}
