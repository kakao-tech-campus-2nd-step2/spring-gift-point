package gift.product.controller;

import gift.product.dto.PointRequestDTO;
import gift.product.model.Member;
import gift.product.service.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/points")
public class AdminPointController {

    private final MemberService memberService;

    @Autowired
    public AdminPointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String pointAdminPage(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        model.addAttribute("pointDTO", new PointRequestDTO());
        return "point-management";
    }

    @PostMapping
    public String addPoint(
        @Valid @ModelAttribute PointRequestDTO pointRequestDTO,
        BindingResult bindingResult,
        Model model) {
        System.out.println("[AdminPointController] addPoint()");
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            model.addAttribute("pointDTO", pointRequestDTO);
            return "point-management";
        }
        memberService.addPoint(pointRequestDTO);
        return "success-add-point";
    }
}
