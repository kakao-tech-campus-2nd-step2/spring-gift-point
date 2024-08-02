package gift.controller;

import gift.dto.Role;
import gift.entity.Member;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@Tag(name = "Admin API", description = "관리자 전용 API")
public class AdminController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    @Operation(summary = "관리자 페이지", description = "관리자 페이지를 반환합니다. 관리자가 아닌 경우 홈 페이지로 리다이렉트합니다.")
    public String adminPage(HttpSession session, Model model) {
        String email = (String) session.getAttribute("user");
        Member member = memberService.getMember(email);
        if (member.getRole() == Role.ADMIN) {
            List<Member> members = memberService.getAllMembers();
            model.addAttribute("members", members);
            return "admin";
        } else {
            return "redirect:/home";
        }
    }

    @DeleteMapping("/members/{id}")
    @Operation(summary = "회원 삭제", description = "주어진 ID를 가진 회원을 삭제합니다.")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/admin";
    }
}
