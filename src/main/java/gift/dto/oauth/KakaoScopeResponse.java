package gift.dto.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "카카오 권한 응답 데이터")
public record KakaoScopeResponse(
    @Schema(description = "권한 리스트")
    List<Scope> scopes
) {

    @Schema(description = "권한 데이터")
    public record Scope(
        @Schema(description = "권한 ID", example = "account_email")
        String id,

        @JsonProperty("display_name")
        @Schema(description = "권한 표시 이름", example = "이메일")
        String displayName,

        @Schema(description = "동의 여부", example = "true")
        boolean agreed
    ) {

    }
}
