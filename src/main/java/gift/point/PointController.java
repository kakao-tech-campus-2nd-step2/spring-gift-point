package gift.point;

import gift.annotation.LoginUser;
import gift.option.OptionRequest;
import gift.option.OptionViewRequest;
import gift.product.Product;
import gift.product.ProductOptionRequest;
import gift.product.ProductRequest;
import gift.user.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PointController {

    private final UserService userService;

    public PointController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("/api/point")
    public PointDTO getPoint(@LoginUser IntegratedUser user){
        if(user instanceof KakaoUser){
            return new PointDTO(((KakaoUser) user).getPoint());
        }
        return new PointDTO();
    }

    @Transactional
    @PutMapping("/api/members/{memberId}/point")
    public String chargePoint(@PathVariable Long memberId, @ModelAttribute PointDTO point){
        System.out.println(point.getPoint());
        User user = userService.getUserById(memberId);
        user.chargePoint(point.getPoint());
        if(user.getUserType() == UserType.KAKAO_USER){
            KakaoUser kakaoUser = userService.getKakaoUserByUser(user);
            kakaoUser.chargePoint(point.getPoint());
        }

        return "redirect:/manager/products";
    }

    @GetMapping("/manager/charge/choseUser")
    public String choseUserView(Model model){
        model.addAttribute("users", userService.getAllUsers());
        return "ChoseUser";
    }

    @PostMapping(value = "/manager/charge/choseUser")
    public String choseUser(@RequestParam String memberId, RedirectAttributes redirectAttributes){
        System.out.println(memberId);
        redirectAttributes.addAttribute("memberId", memberId);

        return "redirect:/api/members/{memberId}/point";
    }

    @GetMapping("/api/members/{memberId}/point")
    public String chargePointView(Model model){
        model.addAttribute("point", new PointDTO());
        return "ChargePoint";
    }

}
