package gift.dto;

import gift.validation.ValidName;
import gift.vo.Option;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "옵션 응답 DTO")
public record OptionResponseDto(

    @Schema(description = "옵션 ID")
    Long id,

    @ValidName
    @NotEmpty(message = "옵션명을 입력해 주세요.")
    @Schema(description = "옵션명")
    String name,

    @Schema(description = "옵션 수량")
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