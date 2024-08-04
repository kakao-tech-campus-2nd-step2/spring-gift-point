package gift.controller.product;

import gift.global.auth.Authorization;
import gift.model.member.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductPageController {

    @Authorization(role = Role.ADMIN)
    @GetMapping("/admin")
    public String admin() {
        return "adminPage";
    }

}
