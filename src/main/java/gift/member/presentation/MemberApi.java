package gift.member.presentation;

import gift.member.presentation.request.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Tag(name = "회원 API")
public interface MemberApi {

    @Operation(summary = "회원 가입")
    @PostMapping("/join")
    ResponseEntity<?> join(
            @Parameter(description = "회원 가입 요청 정보", required = true)
            @RequestBody MemberJoinRequest request
    );

    @Operation(summary = "회원 로그인")
    @PostMapping("/login")
    ResponseEntity<?> login(
            @Parameter(description = "회원 로그인 요청 정보", required = true)
            @RequestBody MemberLoginRequest request
    );

    @Operation(summary = "카카오 로그인 주소로 Redirect")
    @GetMapping("/login/kakao")
    void kakaoLogin(
            @Parameter(hidden = true) HttpServletResponse response
    ) throws IOException;

    @Operation(summary = "카카오 로그인 콜백")
    @GetMapping("/login/kakao/callback")
    ResponseEntity<?> kakaoLoginCallback(
            @Parameter(description = "카카오 로그인 서비스로부터 받은 인증 코드", required = true)
            @RequestParam("code") String code
    );

    @Operation(summary = "회원 정보 조회")
    @GetMapping("/{id}")
    ResponseEntity<MemberControllerResponse> findById(
            @Parameter(
                    description = "회원 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("id") Long memberId
    );

    @Operation(summary = "전체 회원 정보 조회")
    @GetMapping
    ResponseEntity<List<MemberControllerResponse>> findAll();

    @Operation(summary = "이메일 업데이트")
    @PutMapping("/email")
    ResponseEntity<?> updateEmail(
            @Parameter(description = "이메일 업데이트 요청 정보", required = true)
            @RequestBody MemberEmailUpdateRequest request,
            @Parameter(hidden = true) ResolvedMember resolvedMember
    );

    @Operation(summary = "비밀번호 업데이트")
    @PutMapping("/password")
    ResponseEntity<?> updatePassword(
            @Parameter(description = "비밀번호 업데이트 요청 정보", required = true)
            @RequestBody MemberPasswordUpdateRequest request,
            @Parameter(hidden = true) ResolvedMember resolvedMember
    );

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping
    ResponseEntity<?> delete(
            @Parameter(hidden = true) ResolvedMember resolvedMember
    );
}
