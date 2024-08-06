package gift.controller.adminController;

import gift.dto.MemberResponseDto;
import gift.dto.PointUpdateDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        model.addAttribute("requestDto", new PointUpdateDto());
        return "member";
    }

    @PostMapping
    public String updatePoint(@Valid @ModelAttribute("requestDto") PointUpdateDto requestDto, BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) {
            List<MemberResponseDto> members = memberService.getAllMemberInfo();
            model.addAttribute("members", members);
            return "member";
        }
        memberService.updatePoint(requestDto.getId(), requestDto.getPoint());
        return "redirect:/admin/points";
    }
}
