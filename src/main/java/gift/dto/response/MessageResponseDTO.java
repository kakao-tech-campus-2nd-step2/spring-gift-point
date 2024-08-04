package gift.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class MessageResponseDTO {
    @Schema(description = "메시지", nullable = false, example = "메시지 입니다")
    String message;

    public MessageResponseDTO(String message) {
        this.message = message;
    }

    public MessageResponseDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
