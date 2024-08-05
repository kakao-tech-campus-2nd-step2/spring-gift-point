package gift.controller.Point;

import gift.dto.point.MyPointResponse;
import gift.model.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class PointController {


    @GetMapping("/point")
    public ResponseEntity<MyPointResponse.Info> getUserPoint(@RequestAttribute("user") User user) {
        return ResponseEntity.ok(MyPointResponse.Info.fromEntity(user));
    }

}
