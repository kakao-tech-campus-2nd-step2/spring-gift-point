package gift.controller.th;

import gift.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MemberListController {

    MemberService memberService;

    public MemberListController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/user/list/th")
    public ModelAndView thUserList() {
        ModelAndView mav = new ModelAndView("user-list");
        mav.addObject("userModel", memberService.getAll());
        return mav;
    }
}
