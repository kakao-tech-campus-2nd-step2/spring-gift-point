package gift.domain.option.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "옵션 응답 Dto")
public record OptionResponse(
    @Schema(description = "옵션 Id")
    Long id,
    @Schema(description = "옵션 이름")
    String name,
    @Schema(description = "옵션 수량")
    int quantity
) {

}
