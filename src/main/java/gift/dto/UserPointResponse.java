package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for User Point")
public record UserPointResponse(

    @Schema(description = "User Point", example = "10000")
    Integer point
) {

}
