package gift.admin.controller;

import gift.member.dto.PointDto;
import gift.member.entity.Member;
import gift.member.service.MemberService;
import gift.member.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final MemberService memberService;
    private final PointService pointService;

    @Autowired
    public AdminMemberController(MemberService memberService, PointService pointService) {
        this.memberService = memberService;
        this.pointService = pointService;
    }

    @GetMapping
    public String getProducts(Model model, Pageable pageable) {
        model.addAttribute("pages", memberService.findMembers(pageable));
        return "members";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/{email}/points")
    public String addPoint(@PathVariable("email") String email, @RequestBody PointDto pointDto) {
        Member member = memberService.findMember(email).toEntity();
        pointService.addPoint(member, pointDto.point());
        return "redirect:/admin/members";
    }
}
