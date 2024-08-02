package gift.controller.api;

import gift.dto.giftorder.GiftOrderPageResponse;
import gift.dto.giftorder.GiftOrderRequest;
import gift.dto.giftorder.GiftOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "상품 주문 API")
public interface GiftOrderApi {

    @Operation(summary = "회원의 새 주문을 생성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문 생성 성공", content = @Content(schema = @Schema(implementation = GiftOrderResponse.class))),
            @ApiResponse(responseCode = "400", description = "주문 생성 실패(사유 : 사용할 수 있는 포인트보다 더 많은 포인트가 입력되었거나 주문 정보가 잘못되었습니다.)", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "주문 생성 실패(사유 : 카카오 토큰이 만료되었거나, 허용되지 않은 요청입니다.)", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<GiftOrderResponse> orderOption(Long memberId, GiftOrderRequest giftOrderRequest);

    @Operation(summary = "회원의 특정 주문을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 주문 조회 성공", content = @Content(schema = @Schema(implementation = GiftOrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<GiftOrderResponse> getOrder(Long id);

    @Operation(summary = "회원의 모든 주문을 페이지 단위로 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 주문 조회 성공", content = @Content(schema = @Schema(implementation = GiftOrderPageResponse.class))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류", content = @Content(schema = @Schema(hidden = true)))
    })
    ResponseEntity<GiftOrderPageResponse> getOrders(Long memberId, Pageable pageable);

    @Operation(summary = "특정 주문을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "주문 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "404", description = "주문 삭제 실패(사유 : 존재하지 않는 ID 입니다.)"),
            @ApiResponse(responseCode = "500", description = "내부 서버의 오류")
    })
    ResponseEntity<Void> deleteOrder(Long id);
}
