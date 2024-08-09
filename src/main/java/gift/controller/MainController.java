package gift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MainController {
    @GetMapping
    public String redirectMain(){
        return "redirect:/api";
    }

    @GetMapping("/api")
    public String mainView(){
        return "main";
    }
}
