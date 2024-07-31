package gift.controller;

import gift.component.kakao.KakaoProperties;
import gift.vo.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ViewController {

    private final Map<Long, Product> products = new HashMap<>();
    private final KakaoProperties kakaoProperties;

    public ViewController(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/admin")
    public String index(Model model) {
        List<Product> productList = new ArrayList<>(products.values());
        model.addAttribute("products", productList);
        return "admin";
    }

    @GetMapping("/join")
    public String join(Model model) {
        return "join";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("kakaoClientId", kakaoProperties.kakaoClientId());
        model.addAttribute("kakaoRedirectUrl", kakaoProperties.kakaoRedirectUrl());
        return "login";
    }

    @GetMapping("/wish")
    public String wishlist(Model model) {
        return "wishlist";
    }
}
