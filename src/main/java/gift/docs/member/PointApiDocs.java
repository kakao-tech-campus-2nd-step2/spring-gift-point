package gift.docs.member;

import gift.member.presentation.dto.PointRequest;
import gift.member.presentation.dto.PointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;

public interface PointApiDocs {

    @Operation(summary = "포인트 충전")
    public ResponseEntity<Void> addPoint(
        @Parameter(hidden = true) Long memberId,
        PointRequest.Init pointRequestInit);

    @Operation(summary = "포인트 조회")
    public ResponseEntity<PointResponse.Init> getPoint(
        @Parameter(hidden = true) Long memberId);

}
