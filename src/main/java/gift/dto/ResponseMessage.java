package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for messages")
public record ResponseMessage(
    @Schema(description = "Response message", example = "삭제 or 수정되었습니다.")
    String message) {

}
