package gift.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Tag(name = "관리자 페이지", description = "관리자 페이지를 불러옵니다")
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String listProducts() {
        return "admin";
    }
}
