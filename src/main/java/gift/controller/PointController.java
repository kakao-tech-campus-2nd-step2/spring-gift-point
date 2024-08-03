package gift.controller;

import gift.config.LoginUser;
import gift.dto.UserPointResponse;
import gift.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "User Point Management", description = "APIs for managing user point")
public class PointController {

    @GetMapping("/points")
    @Operation(summary = "사용자 포인트 조회", description = "사용자의 잔여 포인트를 조회합니다.",
        responses = @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserPointResponse.class))))
    public ResponseEntity<UserPointResponse> getUserPoint(
        @Parameter(hidden = true) @LoginUser User user) {
        UserPointResponse userPointResponse = new UserPointResponse(user.getPoint());
        return ResponseEntity.ok().body(userPointResponse);
    }

}
