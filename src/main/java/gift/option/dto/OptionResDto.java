package gift.option.dto;

import gift.option.entity.Option;

public record OptionResDto(
        Long id,
        String name,
        Integer quantity
) {

    public OptionResDto(Option option) {
        this(option.getId(), option.getName(), option.getQuantity());
    }
}
