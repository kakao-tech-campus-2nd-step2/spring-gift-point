package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.domain.Member;
import gift.service.KakaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;

@RestController
@RequestMapping("/api/kakao")
public class KakaoController {
    private KakaoService kakaoService;

    public KakaoController(KakaoService kakaoService){
        this.kakaoService = kakaoService;
    }



}
