package gift.controller;

import gift.dto.point.PointRequest;
import gift.dto.point.PointResponse;
import gift.service.PointService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/points")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @PostMapping
    public ResponseEntity<PointResponse> addPoint(@RequestAttribute("memberId") Long memberId, @Valid @RequestBody PointRequest pointRequest) {
        var point = pointService.addPoint(memberId, pointRequest.point());
        return ResponseEntity.created(URI.create("/api/points")).body(point);
    }

    @GetMapping
    public ResponseEntity<PointResponse> getPoint(@RequestAttribute("memberId") Long memberId) {
        var point = pointService.getPoint(memberId);
        return ResponseEntity.ok(point);
    }
}
