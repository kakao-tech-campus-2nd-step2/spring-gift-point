package gift.controller;

import gift.classes.RequestState.SecureRequestStateDTO;
import gift.dto.TokenDto;
import gift.services.KaKaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/kakao")
@Tag(name = "KaKaoController", description = "KaKao API")
public class KaKaoController {

    private final KaKaoService kaKaoService;

    public KaKaoController(KaKaoService kaKaoService) {
        this.kaKaoService = kaKaoService;
    }

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인", description = "카카오로 로그인 할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 토큰을 받음"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 - 로그인 URL 생성 실패"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 요청"),
        @ApiResponse(responseCode = "403", description = "해당 API에 대한 요청 권한이 없는 경우"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생"),
        @ApiResponse(responseCode = "503", description = "서비스 점검중")
    })
    public String login(Model model) {
        model.addAttribute("kakaoUrl", kaKaoService.getKaKaoLogin());

        return "kakaologin";
    }

    @PostMapping("/login")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 후 콜백을 처리하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 콜백을 처리함"),
        @ApiResponse(responseCode = "400", description = "인증 코드가 없음"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 인증 코드로 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public ResponseEntity<SecureRequestStateDTO> callback(HttpServletRequest request) throws Exception {
        String token = kaKaoService.getKaKaoToken(request.getParameter("code"));

        return ResponseEntity.ok().body(new SecureRequestStateDTO(
            HttpStatus.OK,
            "카카오 로그인에 성공했습니다.",
            new TokenDto(token)
        ));
    }

}
