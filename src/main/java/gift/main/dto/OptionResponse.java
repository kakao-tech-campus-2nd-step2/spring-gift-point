package gift.main.dto;

import gift.main.entity.Option;

public record OptionResponse(
        Long id,
        String name,
        int quantity) {

    public OptionResponse(Option option) {
        this(
                option.getId(),
                option.getOptionName(),
                option.getQuantity());
    }

}
