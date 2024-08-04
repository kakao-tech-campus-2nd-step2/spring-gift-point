package gift.point;

import gift.annotation.LoginUser;
import gift.user.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PointController {

    private final UserService userService;
    private final PointService pointService;

    public PointController(UserService userService, PointService pointService) {
        this.userService = userService;
        this.pointService = pointService;
    }

    @ResponseBody
    @GetMapping("/api/point")
    public PointDTO getPoint(@LoginUser IntegratedUser user){
        return new PointDTO(user.getPoint());
    }

    @Transactional
    @PutMapping("/api/members/{memberId}/point")
    public String chargePoint(@PathVariable Long memberId, @ModelAttribute PointDTO point){
        pointService.chargeUserPoint(memberId, point);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/charge/choseUser")
    public String choseUserView(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "ChoseUser";
    }

    @PostMapping(value = "/admin/charge/choseUser")
    public String choseUser(@RequestParam String memberId, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("memberId", memberId);

        return "redirect:/api/members/{memberId}/point";
    }

    @GetMapping("/api/members/{memberId}/point")
    public String chargePointView(Model model){
        model.addAttribute("point", new PointDTO());
        return "ChargePoint";
    }

}
