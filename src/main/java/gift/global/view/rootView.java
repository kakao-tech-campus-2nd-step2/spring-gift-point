package gift.global.view;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class rootView {
    @GetMapping
    public String showAdminPageGet(){
        return "token_save";
    }
    @PostMapping
    public String showAdminPagePost(@RequestParam("token") String token){
        System.out.println(token);
        return "redirect:/admin/products";
    }
}
