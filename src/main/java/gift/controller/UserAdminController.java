package gift.controller;

import gift.entity.User;
import gift.exception.UserAuthException;
import gift.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<User> userPage = userService.getAllUsers(pageable);
        model.addAttribute("userPage", userPage);
        model.addAttribute("sortBy", sortBy);
        return "user-list";
    }

    @GetMapping("/edit/{id}")
    public String editUserPointsForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-edit";
    }

    @PostMapping("/edit/{id}")
    public String editUserPoints(@PathVariable("id") Long id,
        @RequestParam("point") Integer point,
        @RequestParam("password") String password,
        Model model) {
        try {
            userService.updateUserPoint(id, point, password);
        } catch (UserAuthException e) {
            model.addAttribute("error", e.getMessage());
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            return "user-edit";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<Void> healthcheck() {
        return ResponseEntity.ok().build();
    }
}
