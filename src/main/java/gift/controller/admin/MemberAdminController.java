package gift.controller.admin;

import gift.dto.member.MemberEditRequest;
import gift.dto.member.MemberRegisterRequest;
import gift.dto.member.MemberResponse;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        model.addAttribute("members", memberService.getAllMembers());
        return "members";
    }

    @GetMapping("/new")
    public String showAddMemberForm(Model model) {
        model.addAttribute("member", new MemberRegisterRequest("", ""));
        return "member_form";
    }

    @PostMapping
    public String addMember(
        @Valid @ModelAttribute("member") MemberRegisterRequest memberRegisterRequest,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            return "member_form";
        }
        memberService.registerMember(memberRegisterRequest);
        return "redirect:/admin/members";
    }

    @GetMapping("/{id}/edit")
    public String showEditMemberForm(@PathVariable("id") Long id, Model model) {
        MemberResponse memberResponse = memberService.getMemberById(id);
        model.addAttribute(
            "member",
            new MemberEditRequest(memberResponse.id(), memberResponse.email(), null)
        );
        return "member_edit";
    }

    @PutMapping("/{id}")
    public String updateMember(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute MemberEditRequest memberEditRequest,
        BindingResult result,
        Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("member", memberEditRequest);
            model.addAttribute("org.springframework.validation.BindingResult.member", result);
            return "member_edit";
        }
        memberService.updateMember(id, memberEditRequest);
        return "redirect:/admin/members";
    }

    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }
}
