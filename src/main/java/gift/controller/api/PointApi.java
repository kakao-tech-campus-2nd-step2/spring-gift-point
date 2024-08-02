package gift.controller.api;

import gift.dto.point.PointRequest;
import gift.dto.point.PointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "포인트 API")
public interface PointApi {

    @Operation(summary = "회원의 포인트를 추가한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 추가 성공", content = @Content(schema = @Schema(implementation = PointResponse.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<PointResponse> addPoint(Long memberId, PointRequest pointRequest);

    @Operation(summary = "회원의 사용 가능한 포인트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "포인트 조회 성공", content = @Content(schema = @Schema(implementation = PointResponse.class))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<PointResponse> getPoint(Long memberId);
}
