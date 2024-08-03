package gift.controller.admin;

import gift.dto.response.MemberInfoResponse;
import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MemberAdminController {

    private final MemberService memberService;

    public MemberAdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public String showMembers(Model model){
        List<MemberInfoResponse> allMemberInfo = memberService.getAllMember();
        model.addAttribute("members", allMemberInfo);
        return "version-SSR/members";
    }

    @GetMapping("/members/point")
    public String  updatePoint(@RequestParam Long memberId, @RequestParam int newPoint) {
        memberService.updateMemberPoint(memberId, newPoint);
        return "redirect:/members";
    }
}
