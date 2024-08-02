//package gift.user.controller;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//@RestController
//@Tag(name = "Kakao API", description = "카카오 로그인 관련 API")
//
//public class KakaoApiController {
//
//  private final KakaoApiService kakaoApiService;
//
//  public KakaoApiController(KakaoApiService kakaoApiService) {
//    this.kakaoApiService = kakaoApiService;
//  }
//
//  @GetMapping("/kakao/callback")
//  @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 콜백을 처리하고 사용자 정보를 저장 또는 업데이트")
//  public Mono<String> authenticate(
//      @RequestParam @Parameter(description = "카카오 인가 코드", required = true) String code) {
//    return kakaoApiService.getKakaoToken(code)
//        .flatMap(token -> {
//          String accessToken = parseAccessToken(token);
//          return kakaoApiService.getKakaoUserInfo(accessToken);
//        })
//        .flatMap(userInfo -> kakaoApiService.saveOrUpdateUser(userInfo)
//            .then(Mono.just("카카오 로그인 성공")))
//        .onErrorResume(e -> Mono.just("카카오 로그인 중 오류가 발생했습니다: " + e.getMessage()));
//  }
//
//  private String parseAccessToken(String token) {
//    int startIndex = token.indexOf("access_token\":\"") + "access_token\":\"".length();
//    int endIndex = token.indexOf("\"", startIndex);
//    return token.substring(startIndex, endIndex);
//  }
//}
//