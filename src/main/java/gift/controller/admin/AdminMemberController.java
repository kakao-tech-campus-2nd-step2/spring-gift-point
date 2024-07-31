package gift.controller.admin;

import gift.controller.dto.request.MemberRequest;
import gift.controller.dto.response.MemberResponse;
import gift.controller.dto.response.PagingResponse;
import gift.service.MemberService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {
    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    public String getMembers(Model model,
             @PageableDefault(size = 10)Pageable pageable) {
        PagingResponse<MemberResponse> members = memberService.findAllMemberPaging(pageable);
        model.addAttribute("members", members);
        return "member/members";
    }

    @GetMapping("/new")
    public String newMember() {
        return "member/newMember";
    }

    @GetMapping("/{id}")
    public String updateMember(@PathVariable("id") @NotNull @Min(1) Long id, Model model) {
        MemberResponse member = memberService.findById(id);
        model.addAttribute("member", member);
        return "member/editMember";
    }

    @PostMapping("")
    public String newMember(@ModelAttribute MemberRequest request) {
        memberService.signUp(request.email(), request.password(), request.role());
        return "redirect:/admin/member";
    }

    @PutMapping("/{id}")
    public String updateMember(@PathVariable("id") @NotNull @Min(1) Long id,
                               @ModelAttribute MemberRequest request) {
        memberService.updateById(id, request);
        return "redirect:/admin/member";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") @NotNull @Min(1) Long id) {
        memberService.deleteById(id);
        return "redirect:/admin/member";
    }
}
