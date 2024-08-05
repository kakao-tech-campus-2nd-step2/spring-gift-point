package gift.controller;

import gift.model.Member;
import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {
    private final MemberService memberService;

    public AdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/web/admin/points")
    public String getPointsPage(Model model) {
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "points";
    }

    @PostMapping("/admin/points/update")
    public String updatePoints(@RequestParam String email, @RequestParam int points) {
        memberService.updateMemberPoints(email, points);
        return "redirect:/web/admin/points";
    }
}
