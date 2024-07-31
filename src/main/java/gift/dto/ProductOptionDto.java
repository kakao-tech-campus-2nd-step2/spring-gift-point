package gift.dto;

import gift.domain.Option;

public class ProductOptionDto {
    private Long id;
    private String name;
    private Integer quantity;

    public ProductOptionDto(Option option) {
        this.id = option.getId();
        this.name = option.getName();
        this.quantity = option.getQuantity();
    }
}


