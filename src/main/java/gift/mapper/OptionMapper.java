package gift.mapper;

import gift.dto.OptionResponseDto;
import gift.entity.Option;

public class OptionMapper {
    public static OptionResponseDto toOptionResponseDto(Option option) {
        return new OptionResponseDto(
                option.getId(),
                option.getName().getValue()
        );
    }
}
