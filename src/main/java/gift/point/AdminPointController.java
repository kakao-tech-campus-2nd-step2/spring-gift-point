package gift.point;

import gift.member.MemberService;
import gift.member.model.Member;
import gift.point.model.PointRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/points")
public class AdminPointController {

    private final PointService pointService;
    private final MemberService memberService;

    public AdminPointController(PointService pointService, MemberService memberService) {
        this.pointService = pointService;
        this.memberService = memberService;
    }

    @GetMapping
    public String getAllPoints(Model model,
        @PageableDefault(size = 10, sort = "id", direction = Direction.ASC) Pageable pageable) {
        Page<Member> members = pointService.getAllPoints(pageable);
        model.addAttribute("members", members);
        return "points";
    }

    @GetMapping("/add/{id}")
    public String addPointForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("member", memberService.getMemberById(id));
        return "add-point-form";
    }

    @PostMapping("/add/{id}")
    public String addPoint(@PathVariable("id") Long id,
        @ModelAttribute PointRequest pointRequest,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-point-form";
        }
        pointService.addPoint(id, pointRequest);
        return "redirect:/admin/points";
    }
}
