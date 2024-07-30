package gift.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "회원 API")
public interface MemberApi {

    @Operation(summary = "회원을 인증하고 탈퇴를 진행한다.")
    ResponseEntity<Void> deleteMember(Long memberId);
}
