package gift.controller;

import gift.dto.GiftOrderRequestDto;
import gift.dto.GiftOrderResponseDto;
import gift.service.BasicTokenService;
import gift.service.GiftOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "gift-order", description = "선물 주문 API")
@RequestMapping("/api/orders")
@RestController
public class GiftOrderController {
    private final GiftOrderService giftOrderService;
    private final BasicTokenService basicTokenService;

    public GiftOrderController(GiftOrderService giftOrderService, BasicTokenService basicTokenService) {
        this.giftOrderService = giftOrderService;
        this.basicTokenService = basicTokenService;
    }

    @PostMapping
    @Operation(summary = "메세지와 함께 주문하기", description = "주문과 입력받은 메세지로 주문을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메세지를 포함한 주문 등록 성공", content = @Content(schema = @Schema(implementation = GiftOrderResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스 접근", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<GiftOrderResponseDto> placeOrderWithMessage(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody GiftOrderRequestDto giftOrderRequestDto
    ) throws IllegalAccessException {

        Long orderMemberId = basicTokenService.getUserIdByDecodeTokenValue(authorizationHeader);
        GiftOrderResponseDto giftOrderResponseDto = giftOrderService.placeOrderWithMessage(giftOrderRequestDto, orderMemberId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(giftOrderResponseDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(giftOrderResponseDto);
    }
}
