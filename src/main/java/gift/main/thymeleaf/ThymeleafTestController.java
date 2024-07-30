package gift.main.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafTestController {

    @GetMapping({"", "/"})
    public String test(Model model) {
        model.addAttribute("title", "멋지게 인사하는 법");
        model.addAttribute("text", "자이언티의 노래");

        List<String> list = new ArrayList<>();

        list.add("안녕하세요");
        list.add("잘보이나여?");
        list.add("그래요..");
        list.add("행복하세용");


        model.addAttribute("list", list);
        return "thymeleaf/Thymeleaf";
    }
}
