package gift.dto.oauth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 연결 해제 응답 데이터")
public record KakaoUnlinkResponse(
    @Schema(description = "연결 해제된 사용자 ID", example = "123456789")
    Long id
) {

}
