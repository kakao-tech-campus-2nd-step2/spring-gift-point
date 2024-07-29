package gift.option.application;

import gift.option.domain.Option;

public record OptionServiceResponse(
        Long id,
        String name,
        Integer quantity
){
    public static OptionServiceResponse from(Option option) {
        return new OptionServiceResponse(
                option.getId(),
                option.getName(),
                option.getQuantity()
        );
    }
}
