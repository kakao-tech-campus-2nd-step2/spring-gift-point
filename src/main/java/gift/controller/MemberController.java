package gift.controller;

import gift.dto.response.MemberResponse;
import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public String listMembers(Model model) {
        List<MemberResponse> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "members/list";
    }

    @GetMapping("/members/{id}/addPoints")
    public String showAddPointsForm(@PathVariable Long id, Model model) {
        MemberResponse member = memberService.getMemberById(id);
        model.addAttribute("member", member);
        return "members/addPoints";
    }

    @PostMapping("/members/{id}/points")
    public String addPoints(@PathVariable Long id, @RequestParam int points, Model model) {
        memberService.addMemberPoints(id, points);
        return "redirect:/members";
    }
}
