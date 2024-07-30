package gift.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 API")
public interface MemberApi {

    @Operation(summary = "회원을 인증하고 탈퇴를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "404", description = "회원 탈퇴 실패(사유 : 존재하지 않는 ID 입니다.)")
    })
    ResponseEntity<Void> deleteMember(Long memberId);
}
