package gift.controller;

import gift.model.PointRequest;
import gift.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/admin/points")
@Tag(name = "Point Management", description = "APIs for managing user points")
public class PointController {

    private final UserService userService;

    public PointController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showChargePointsForm() {
        return "Charge_points"; // 이 부분은 템플릿 파일 이름을 의미합니다.
    }

    @PostMapping
    @ResponseBody
    @Operation(summary = "Charge points to a user's account", description = "This API charges points to a user's account.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Points charged successfully."),
        @ApiResponse(responseCode = "404", description = "User not found."),
        @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<Map<String, String>> chargePoints(@RequestBody PointRequest pointRequest) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.chargePoints(pointRequest.getUserId(), pointRequest.getAmount());
            response.put("message", "Points charged successfully.");  // JSON 형태의 메시지
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());  // 오류 메시지를 JSON 형태로
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            response.put("error", "Internal server error.");
            return ResponseEntity.status(500).body(response);
        }
    }


}