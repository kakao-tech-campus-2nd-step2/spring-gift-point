package gift.controller;

import gift.domain.Member;
import gift.dto.PointUpdateRequestDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/members")
public class MemberAdminController {

    private final MemberService memberService;

    public MemberAdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String getAllMembers(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "admin-member";
    }

    @GetMapping("/updatePoints/{memberId}")
    public String showAddPointsForm(@PathVariable("memberId") Long id, Model model) {
        model.addAttribute("memberId", id);
        return "add-point"; // 포인트 추가 폼을 보여주는 템플릿 이름
    }

    @PostMapping("/updatePoints")
    public String updatePoints(@Valid PointUpdateRequestDto request) {
        memberService.updatePoint(request);
        return "redirect:/admin/members";
    }
}
