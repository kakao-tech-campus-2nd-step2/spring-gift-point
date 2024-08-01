package gift.option.dto;

import gift.option.domain.Option;

import java.util.List;
import java.util.stream.Collectors;

public record OptionListResponseDto(List<OptionResponseDto> options) {
    public static OptionListResponseDto optionListToOptionListResponseDto(List<Option> options) {
        List<OptionResponseDto> newOptionResponseDtos = options.stream()
                .map(option -> new OptionResponseDto(option.getId(), option.getName(), option.getQuantity()))
                .collect(Collectors.toList());

        return new OptionListResponseDto(newOptionResponseDtos);
    }
}
