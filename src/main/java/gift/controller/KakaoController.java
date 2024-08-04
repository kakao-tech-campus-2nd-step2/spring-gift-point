package gift.controller;

import gift.service.KakaoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kakao")
public class KakaoController {
    private KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService){
        this.kakaoService = kakaoService;
    }

}
