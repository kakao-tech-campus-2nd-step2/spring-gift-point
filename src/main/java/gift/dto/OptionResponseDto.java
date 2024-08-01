package gift.dto;

import gift.domain.option.Option;

public class OptionResponseDto {

    private final Long id;
    private final String name;

    private final Integer quantity;

    public OptionResponseDto(Option option) {
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
