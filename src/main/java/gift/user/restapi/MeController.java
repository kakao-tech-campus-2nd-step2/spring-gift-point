package gift.user.restapi;

import gift.advice.ErrorResponse;
import gift.advice.LoggedInUser;
import gift.user.restapi.dto.response.MeResponse;
import gift.user.service.MeService;
import gift.user.service.UserInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "내 정보")
public class MeController {
    private final MeService meService;

    @Autowired
    public MeController(MeService meService) {
        this.meService = meService;
    }

    @GetMapping("/api/members/me")
    @Operation(summary = "내 정보 조회", description = "내 정보를 조회합니다.")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "내 정보를 조회합니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MeResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "사용자를 찾을 수 없습니다.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    public MeResponse getUser(@LoggedInUser Long userId) {
        UserInfoDto userInfo = meService.getUserInfo(userId);

        return MeResponse.of(userInfo);
    }
}
