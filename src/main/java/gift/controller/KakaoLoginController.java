package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import gift.dto.KakaoProperties;
import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.KakaoUserInfoResponse;
import gift.service.KakaoApiService;
import gift.service.KakaoTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "kakao_login", description = "카카오 API")
public class KakaoLoginController {

    private final KakaoProperties kakaoProperties;
    private final KakaoApiService kakaoApiService;
    private final KakaoTokenService kakaoTokenService; 

    public KakaoLoginController(KakaoProperties kakaoProperties, KakaoApiService kakaoApiService, KakaoTokenService kakaoTokenService){
        this.kakaoProperties = kakaoProperties;
        this.kakaoApiService = kakaoApiService;
        this.kakaoTokenService = kakaoTokenService;
    }

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인 화면", description = "카카오 로그인 페이지를 띄웁니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카카오 로그인 화면 이동 성공")
    })
    public String login(Model model){
        model.addAttribute("kakaoApiKey", kakaoProperties.getApiKey());
        model.addAttribute("redirectUri", kakaoProperties.getRedirectUri());
        return "login";
    }

    @RequestMapping("/login/code")
    @Operation(summary = "카카오 로그인", description = "파라미터로 받은 인가 코드로 카카오 로그인을 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "카카오 로그인 성공"),
        @ApiResponse(responseCode = "400", description = "파라미터 오류"),
        @ApiResponse(responseCode = "401", description = "인증 오류"),
        @ApiResponse(responseCode = "403", description = "권한 오류"),
        @ApiResponse(responseCode = "429", description = "쿼터 초과"),
        @ApiResponse(responseCode = "500", description = "시스템 오류"),
        @ApiResponse(responseCode = "502", description = "시스템 오류"),
        @ApiResponse(responseCode = "503", description = "서비스 점검중")
    })
    public ResponseEntity<String> kakaoLogin(@RequestParam String code) {

        KakaoTokenResponse response = kakaoApiService.getToken(code);

        KakaoUserInfoResponse kakaoUserInfoResponse = kakaoApiService.getUserInfo(response.getAccessToken());
        
        //temporary email
        kakaoTokenService.saveKakaoToken(kakaoUserInfoResponse.getId() + "@kakao.com", response);
        
        return new ResponseEntity<>(response.getAccessToken(), HttpStatus.OK);
    }
    
}
