package gift.dto;

import gift.domain.Option;

public class ProductOptionDto {
    private final Long id;
    private final String name;
    private final Integer quantity;

    public ProductOptionDto(Option option) {
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
    public Integer getQuantity() {
        return quantity;
    }
}


