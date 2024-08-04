package gift.controller;

import gift.dto.AddPointsRequest;
import gift.dto.PointResponse;
import gift.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/points")
public class PointController {

    private final MemberService memberService;
    @Autowired
    public PointController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<?> getPoints(HttpServletRequest request) {
        Long memberId = (Long) request.getAttribute("memberId");

        int points = memberService.getMemberPoints(memberId);

        return ResponseEntity.ok(new PointResponse(points));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPoints(@RequestBody AddPointsRequest addPointsRequest) {
        try {
            memberService.addPointsToMember(addPointsRequest.memberId(), addPointsRequest.points());
            return ResponseEntity.ok("Points added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
