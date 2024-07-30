package gift.product.docs;

import gift.product.dto.OrderRequestDTO;
import gift.product.dto.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@Tag(name = "Order", description = "API 컨트롤러")
public interface OrderControllerDocs {

    @Operation(summary = "주문하기", description = "옵션을 주문합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "주문 성공",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = OrderResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "주문 수량이 정상적이지 않은 경우(0 이하)"),
        @ApiResponse(responseCode = "401", description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"),
        @ApiResponse(responseCode = "404", description = "주문 내역이 올바르지 않은 경우"),
        @ApiResponse(responseCode = "500", description = "주문 내역에는 문제가 없으나, 서버가 이를 올바르게 처리하지 못한 경우"),
        @ApiResponse(responseCode = "502", description = "애플리케이션의 서버가 카카오 서버로부터 올바르지 못한 응답을 받은 경우")})
    ResponseEntity<?> orderProduct(String authorization, OrderRequestDTO orderRequestDTO,BindingResult bindingResult);
}