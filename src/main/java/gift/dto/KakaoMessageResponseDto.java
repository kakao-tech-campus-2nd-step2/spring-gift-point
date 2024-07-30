package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 메세지보내기 응답 DTO ")
public class KakaoMessageResponseDto {
    @Schema(description = "카카오 메세지보내기 요청에 대한 응답 코드")
    private Integer result_code;

    public KakaoMessageResponseDto(Integer result_code) {
        this.result_code = result_code;
    }
}
