package gift.controller;

import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/points")
public class AdminPointController {
    private final MemberService memberService;

    public AdminPointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String getPointPage(Model model) {
        List<MemberResponseDto> members = memberService.getAllMemberInfo();
        model.addAttribute("members", members);
        return "member";
    }

    @PostMapping
    public String updatePoint(@RequestParam Long id, @RequestParam int point) {
        memberService.updatePoint(id, point);
        return "redirect:/admin/points";
    }

}
