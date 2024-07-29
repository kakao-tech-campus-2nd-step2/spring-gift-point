package gift.controller.view;

import gift.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    @Value("${kakao.client-id}")
    private String clientId;
    @Value(("${kakao.redirect-url}"))
    private String redirectUrl;
    @Value(("${kakao.response-type}"))
    private String responseType;

    private final OrderService orderService;

    public UserViewController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("responseType", responseType);
        return "signup";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("clientId", clientId);
        model.addAttribute("redirectUrl", redirectUrl);
        model.addAttribute("responseType", responseType);
        return "login";
    }

    @GetMapping("/kakao")
    public String kakao() {
        return "kakao";
    }

    @GetMapping("/me")
    public String me(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        model.addAttribute("orders", orderService.findAll(email));
        return "me";
    }
}
