package gift.Controller;

import gift.Model.response.PointResponse;
import gift.Service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
public class PointController {
    private final UserService userService;

    public PointController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public PointResponse read(String email){
        return userService.read(email);
    }
}
