package gift.model.response;

import gift.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "API 에러 응답 DTO")
public class ErrorResponseDTO {

    @Schema(description = "에러 메시지", example = "입력값이 유효하지 않습니다.")
    private String message;

    @Schema(description = "상세 에러 정보", example = "{\"email\":\"이메일 형식이 올바르지 않습니다.\", \"password\":\"비밀번호는 8자 이상이어야 합니다.\"}")
    private Map<String, String> errors;

    public ErrorResponseDTO(ErrorCode errorCode, Map<String, String> errors) {
        this.message = errorCode.getMessage();
        this.errors = errors;
    }

    public ErrorResponseDTO() {
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
