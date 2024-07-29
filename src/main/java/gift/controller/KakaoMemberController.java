package gift.controller;

import gift.dto.request.MemberRequest;
import gift.dto.response.TokenResponse;
import gift.service.KakaoService;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import io.swagger.v3.oas.annotations.Operation;

@Controller
public class KakaoMemberController {
    private final KakaoService kakaoService;
    private final MemberService memberService;

    public KakaoMemberController(KakaoService kakaoService, MemberService memberService) {
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @Operation(summary = "로그인 폼을 반환합니다")
    @GetMapping("/login")
    public String showLoginForm() {
        return "members/login";
    }

    @Operation(
            summary = "카카오 로그인 요청을 처리합니다",
            description = "Authorization 헤더에 유효한 카카오 토큰이 있는지 확인합니다. " +
                    "유효한 토큰이 있으면 위시리스트 페이지로 리디렉션하고, " +
                    "유효한 토큰이 없으면 카카오 인증 URL로 리디렉션합니다.",
            responses = {
                    @ApiResponse(responseCode = "302", description = "리디렉션 응답")
            }
    )
    @GetMapping("/members/login/kakao/oauth")
    public String kakaoLogin(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token!=null && kakaoService.isKakaoTokenValid(token.substring(7))){
            return "redirect:/members/wishes";
        }
        return "redirect:" + kakaoService.getKakaoCodeUrl();
    }

    @Operation(
            summary = "카카오 로그인 콜백을 처리하고 사용자 정보를 등록합니다",
            description = "카카오 인증 코드를 받아 카카오 API를 통해 액세스 토큰을 얻고, " +
                    "해당 토큰으로 사용자 이메일을 가져와서 데이터베이스에 사용자 정보를 등록합니다. " +
                    "등록 후, 액세스 토큰을 포함한 응답을 반환합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "성공적으로 사용자 정보를 등록하고 토큰을 반환함")
            }
    )
    @GetMapping("/members/login/kakao/callback")
    public ResponseEntity<TokenResponse> registerKakaoUser(HttpServletRequest servletRequest) {
        String code = servletRequest.getParameter("code");
        String token = kakaoService.getKakaoToken(code);
        String userEmail = kakaoService.getKakaoUserEmail(token);
        memberService.save(new MemberRequest(null,userEmail,null));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(new TokenResponse(token));
    }

    @Operation(summary = "사용자의 위시리스트를 표시합니다")
    @GetMapping("/members/wishes")
    public String showWishlist() {
        return "wishes/list";
    }
}