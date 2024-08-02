package gift.domain.option.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "옵션 리스트 응답 Dto")
public record OptionsResponse(
    @Schema(description = "옵션 리스트")
    List<OptionResponse> options
) {

}
