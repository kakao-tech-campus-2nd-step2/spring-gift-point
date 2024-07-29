package gift.dto;

import gift.entity.Option;

public class OptionResponseDto {

    private final Long id;
    private final String name;
    private final Long quantity;

    public OptionResponseDto(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

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

    public Long getQuantity() {
        return quantity;
    }
}
