package gift.domain.model.dto;

import gift.domain.model.entity.Option;

public class OptionResponseDto {

    private final Long id;
    private final String name;
    private final int quantity;

    public OptionResponseDto(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public static OptionResponseDto toDto(Option option) {
        return new OptionResponseDto(option.getId(), option.getName(), option.getQuantity());
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
}
