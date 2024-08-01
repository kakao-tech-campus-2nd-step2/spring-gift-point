package gift.controller.admin;

import gift.dto.member.MemberEditRequest;
import gift.dto.member.MemberEditResponse;
import gift.dto.member.MemberRegisterRequest;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Admin Page - Member API", description = "관리자 페이지 - 회원 관리 API")
@Controller
@RequestMapping("/admin/members")
public class MemberAdminController {

    private final MemberService memberService;

    public MemberAdminController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "모든 회원 조회", description = "모든 회원 정보를 조회합니다.")
    @GetMapping
    public String getAllMembers(Model model) {
        model.addAttribute("members", memberService.getAllMembers());
        return "members";
    }

    @Operation(summary = "회원 추가 폼", description = "새로운 회원을 추가하는 폼을 보여줍니다.")
    @GetMapping("/new")
    public String showAddMemberForm(Model model) {
        model.addAttribute("member", new MemberRegisterRequest("", ""));
        return "member_form";
    }

    @Operation(summary = "회원 추가", description = "새로운 회원을 시스템에 추가합니다.")
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

    @Operation(summary = "회원 수정 폼", description = "기존 회원 정보를 수정하는 폼을 보여줍니다.")
    @GetMapping("/{id}/edit")
    public String showEditMemberForm(@PathVariable("id") Long id, Model model) {
        MemberEditResponse memberEditResponse = memberService.getMemberById(id);
        model.addAttribute(
            "member",
            new MemberEditRequest(
                memberEditResponse.id(),
                memberEditResponse.email(),
                null,
                memberEditResponse.registerType()
            )
        );
        return "member_edit";
    }

    @Operation(summary = "회원 수정", description = "기존 회원 정보를 수정합니다.")
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

    @Operation(summary = "회원 삭제", description = "기존 회원을 삭제합니다.")
    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin/members";
    }
}
