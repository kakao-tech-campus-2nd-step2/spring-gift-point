package gift.ResponseDto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ResponseResourceErrorDto {
  @Schema(description = "상태 코드", example = "404 Error")
  private String status;
  @Schema(description = "상태 메시지", example = "매핑되는 URI가 없습니다.")
  private String message;
  public ResponseResourceErrorDto() {}
  public ResponseResourceErrorDto(String status, String message) {
    this.status = status;
    this.message = message;
  }

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return message;
  }
}
