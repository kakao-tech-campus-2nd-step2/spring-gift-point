package gift.controller;

import gift.dto.GiftOrderRequestDto;
import gift.dto.GiftOrderResponseDto;
import gift.service.BasicTokenService;
import gift.service.GiftOrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
