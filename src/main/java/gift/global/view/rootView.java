package gift.global.view;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class rootView {
    @GetMapping
    public String showAdminPageGet(){
        return "redirect:/admin/products";
    }
    @PostMapping
    public String showAdminPagePost(){
        return "redirect:/admin/products";
    }
}
