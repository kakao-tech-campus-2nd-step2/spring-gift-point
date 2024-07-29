package gift.controller;

import gift.dto.betweenClient.ResponseDTO;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.service.KakaoAuthService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/oauth/kakao")
public class KakaoAuthController {
    private final KakaoAuthService kakaoAuthService;
    private final JwtUtil jwtUtil;
    private final Logger logger = LoggerFactory.getLogger(KakaoAuthController.class);

    @Autowired
    KakaoAuthController(KakaoAuthService kakaoAuthService, JwtUtil jwtUtil) {
        this.kakaoAuthService = kakaoAuthService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    @Operation(description = "서버가 클라이언트가 제출한 인가코드를 가지고 확인하여 클라이언트에게 자체 토큰을 발급합니다.", tags = "KakaoAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰을 성공적으로 발급하였습니다.",
                    content = @Content(mediaType = "text/html", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 이미 basic 타입 계정에 같은 email이 존재하거나, 잘못된 요청 양식인 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public String loginOrRegister(@RequestParam String code, Model model) {
        try {
            String token = jwtUtil.generateToken(kakaoAuthService.loginOrRegister(code));
            model.addAttribute("token", token);
            model.addAttribute("success", true);
        } catch (BadRequestException e){
            model.addAttribute("message", e.getMessage());
            model.addAttribute("success", false);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            model.addAttribute("message", e.getMessage());
            model.addAttribute("success", false);
        }
        return "loginResult";
    }
}
