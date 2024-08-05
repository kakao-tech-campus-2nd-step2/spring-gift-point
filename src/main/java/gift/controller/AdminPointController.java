package gift.controller;


import gift.entity.Member;
import gift.entity.Point;
import gift.service.MemberService;
import gift.service.PointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(name = "[협업] ADMIN POINT API", description = "[협업] 포인트 관리자 컨트롤러")
public class AdminPointController {

    private final PointService pointService;
    private final MemberService memberService;

    @Autowired
    public AdminPointController(
            PointService pointService,
            MemberService memberService
    ){
        this.pointService = pointService;
        this.memberService = memberService;
    }

    @PostMapping("/{email}/points")
    public String addPoint(@PathVariable("email") String email, @RequestBody Point point) {
        Member member = memberService.getMember(email);
        pointService.addPoint(member, point.getPoint());
        return "redirect:/admin/members";
    }

}
