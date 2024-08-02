package gift.controller;

import gift.config.KakaoAuthProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/kakao/login")
public class KakaoLoginPageController {


    @GetMapping("/page")
    public String loginPage() {
        return "kakao-login";
    }


}
