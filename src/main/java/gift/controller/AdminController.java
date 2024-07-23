package gift.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("")
    public String getMain() {
        return "/admin";
    }

    @GetMapping("/addProductForm")
    public String addProduct() {
        return "/addProductForm";
    }

    @GetMapping("/updateProductForm")
    public String updateProductForm(@RequestParam("ids") List<Long> ids, Model model) {
        model.addAttribute("ids", ids);
        return "/updateProductForm";
    }
}
