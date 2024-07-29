package gift.dto;

import gift.validation.ValidName;
import gift.vo.Option;
import jakarta.validation.constraints.NotEmpty;

public record OptionResponseDto(
    Long id,

    @ValidName
    @NotEmpty(message = "옵션명을 입력해 주세요.")
    String name,

    int quantity
){
    public static OptionResponseDto toOptionResponseDto (Option option) {
        return new OptionResponseDto(
                option.getId(),
                option.getName(),
                option.getQuantity()
        );
    }
}