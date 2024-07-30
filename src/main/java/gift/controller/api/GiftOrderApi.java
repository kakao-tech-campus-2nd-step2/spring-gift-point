package gift.controller.api;

import gift.dto.giftorder.GiftOrderRequest;
import gift.dto.giftorder.GiftOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "상품 주문 API")
public interface GiftOrderApi {

    @Operation(summary = "회원의 새 주문을 생성한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청")
    })
    ResponseEntity<Void> orderOption(Long memberId, GiftOrderRequest giftOrderRequest);

    @Operation(summary = "회원의 특정 주문을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 주문 조회 성공", content = @Content(schema = @Schema(implementation = GiftOrderResponse.class))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청")
    })
    ResponseEntity<GiftOrderResponse> getOrder(Long id);

    @Operation(summary = "회원의 모든 주문을 페이지 단위로 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 주문 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = GiftOrderResponse.class)))),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청")
    })
    ResponseEntity<List<GiftOrderResponse>> getOrders(Long memberId, Pageable pageable);

    @Operation(summary = "특정 주문을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "주문 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "허용되지 않는 요청"),
            @ApiResponse(responseCode = "404", description = "주문 삭제 실패(사유 : 존재하지 않는 ID 입니다.)")
    })
    ResponseEntity<Void> deleteOrder(Long id);
}
