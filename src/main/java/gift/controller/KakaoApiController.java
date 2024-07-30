package gift.controller;

import gift.controller.dto.KakaoApiDTO;
import gift.controller.dto.KakaoApiDTO.KakaoOrderResponse;
import gift.controller.dto.PaginationDTO;
import gift.controller.dto.TokenResponseDTO;
import gift.domain.Wish;
import gift.service.KakaoApiService;
import gift.utils.PaginationUtils;
import gift.utils.config.KakaoProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KakaoApiController {
    private final KakaoApiService kakaoApiService;

    public KakaoApiController(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }
    //user 컨트롤러로 추가
    @GetMapping("/oauth/authorize")
    public String kakaoLogin(){
        String kakaoCode = kakaoApiService.createKakaoCode();
        return "redirect:"+kakaoCode;
    }
    //user 컨트롤러로 추가
    @GetMapping("/oauth/token")
    public ResponseEntity<TokenResponseDTO> kakaoToken(
        @RequestParam(required = false) String code,
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String error_description,
        @RequestParam(required = false) String state){

        TokenResponseDTO kakaoToken = kakaoApiService.createKakaoToken(code, error,
            error_description, state);

        return ResponseEntity.ok(kakaoToken);
    }

    //proudct 컨트롤러로 추가 product/order

    @PostMapping("/orders")
    public ResponseEntity<KakaoApiDTO.KakaoOrderResponse> kakaoOrder(@RequestHeader("Authorization") String token,
        @RequestBody KakaoApiDTO.KakaoOrderRequest kakaoOrderRequest){
        String jwttoken = token.substring(7);
        KakaoOrderResponse kakaoOrderResponse = kakaoApiService.kakaoOrder(kakaoOrderRequest,jwttoken);
        return ResponseEntity.ok(kakaoOrderResponse);
    }

//    @GetMapping("orders")
//    public ResponseEntity<Page<KakaoApiDTO.KakaoOrderResponse>> orderGet(@RequestBody PaginationDTO paginationDTO){
//        Pageable pageable = PaginationUtils.createPageable(paginationDTO, "order");
//
//        Page<Wish> orderList = kakaoApiService.(pageable);
//        return ResponseEntity.ok(orderList);
//    }
}
