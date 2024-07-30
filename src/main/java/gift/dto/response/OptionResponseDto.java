package gift.dto.response;

import gift.domain.Option;

public record OptionResponseDto(
        Long id,

        String name,

        int quantity
){
    public static OptionResponseDto of(Long id, String name, int quantity) {
        return new OptionResponseDto(id, name, quantity);
    }

    public static OptionResponseDto from(Option option) {
        return new OptionResponseDto(option.getId(), option.getName(), option.getQuantity());
    }
}
