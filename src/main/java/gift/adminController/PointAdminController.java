package gift.adminController;

import gift.entity.User;
import gift.service.UserService;
import gift.service.PointService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/points")
public class PointAdminController {

    private final UserService userService;
    private final PointService pointService;

    public PointAdminController(UserService userService, PointService pointService) {
        this.userService = userService;
        this.pointService = pointService;
    }

    @GetMapping
    public String showUserPoints(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "pointAdmin/admin-points";
    }

    @GetMapping("/charge/{userId}")
    public String showChargePointsPage(@PathVariable Long userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "pointAdmin/point-charge";
    }

    @PostMapping("/charge")
    public String chargePoints(@RequestParam Long userId, @RequestParam int points, Model model) {
        pointService.chargePoints(userId, points);
        model.addAttribute("message", "Points have been charged.");
        return "redirect:/admin/points";
    }
}
