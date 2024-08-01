package gift.user.presentation;

import gift.user.application.PointService;
import gift.user.domain.PointResponse;
import gift.util.annotation.JwtAuthenticated;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @JwtAuthenticated
    @GetMapping("/members/point")
    public ResponseEntity<?> getPoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());
        Long point = pointService.getPoint(userId);
        return ResponseEntity.ok(new PointResponse(point));
    }
}
