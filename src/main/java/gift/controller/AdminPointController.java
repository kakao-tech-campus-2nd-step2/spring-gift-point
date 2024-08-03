package gift.controller;

import gift.dto.DomainResponse;
import gift.model.HttpResult;
import gift.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Tag(name = "Admin Point API", description = "APIs for admin point operations")
public class AdminPointController {

    @Autowired
    private PointService pointService;

    @Operation(summary = "포인트 충전 페이지", description = "관리자가 회원에게 포인트를 충전하는 페이지를 보여준다.")
    @GetMapping("/points/charge")
    public String showChargePointsPage(Model model) {
        return "charge-points";
    }

    @Operation(summary = "포인트 충전", description = "관리자가 회원에게 포인트를 충전한다.")
    @PostMapping("/points/charge")
    public ResponseEntity<DomainResponse> chargePoints(@RequestParam String email, @RequestParam int points) {
        pointService.addPointsByEmail(email, points);
        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("pointsAdded", points);
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Points charged successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, List.of(response), HttpStatus.OK), HttpStatus.OK);
    }
}