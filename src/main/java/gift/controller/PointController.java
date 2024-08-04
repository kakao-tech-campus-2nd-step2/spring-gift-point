package gift.controller;

import gift.dto.DomainResponse;
import gift.model.HttpResult;
import gift.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/points")
@Tag(name = "Point API", description = "APIs related to point operations")
public class PointController {

    @Autowired
    private PointService pointService;

    @Operation(summary = "포인트 추가", description = "회원에게 포인트를 추가한다.")
    @PostMapping("/add")
    public ResponseEntity<DomainResponse> addPoints(@RequestParam Long memberId, @RequestParam int points) {
        pointService.addPoints(memberId, points);
        Map<String, Object> response = new HashMap<>();
        response.put("memberId", memberId);
        response.put("pointsAdded", points);
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Points added successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, List.of(response), HttpStatus.OK), HttpStatus.OK);
    }

    @Operation(summary = "포인트 사용", description = "회원의 포인트를 사용한다.")
    @PostMapping("/use")
    public ResponseEntity<DomainResponse> usePoints(@RequestParam Long memberId, @RequestParam int points) {
        try {
            pointService.usePoints(memberId, points);
            Map<String, Object> response = new HashMap<>();
            response.put("memberId", memberId);
            response.put("pointsUsed", points);
            HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Points used successfully");
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(response), HttpStatus.OK), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            HttpResult httpResult = new HttpResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }
}