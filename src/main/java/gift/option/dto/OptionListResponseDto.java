package gift.option.dto;

import gift.option.domain.Option;
import gift.option.domain.OptionCount;
import gift.option.domain.OptionName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record OptionListResponseDto(List<OptionResponseDto> optionResponseDtos) {
    public static OptionListResponseDto optionListToOptionListResponseDto(List<Option> options) {
        List<OptionResponseDto> newOptionResponseDtos = options.stream()
                .map(option -> new OptionResponseDto(option.getId(), option.getName(), option.getCount(), option.getProduct().getId()))
                .collect(Collectors.toList());

        return new OptionListResponseDto(newOptionResponseDtos);
    }
}
