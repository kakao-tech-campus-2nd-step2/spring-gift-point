package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.betweenClient.ResponseDTO;
import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenClient.order.OrderRequestDTO;
import gift.dto.betweenClient.order.OrderResponseDTO;
import gift.entity.Option;
import gift.service.KakaoTokenService;
import gift.service.MemberService;
import gift.service.OptionService;
import gift.service.OrderService;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final KakaoTokenService kakaoTokenService;
    private final MemberService memberService;
    private final OptionService optionService;
    private final WishListService wishListService;
    private final OrderService orderService;

    public OrderController(KakaoTokenService kakaoTokenService, MemberService memberService,
            OptionService optionService, WishListService wishListService, OrderService orderService) {
        this.kakaoTokenService = kakaoTokenService;
        this.memberService = memberService;
        this.optionService = optionService;
        this.wishListService = wishListService;
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(description = "서버가 모든 주문 목록을 제공합니다." +
            " 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.", tags = "Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 목록을 성공적으로 제공합니다. 여기서 응답 객체 내의 content는 OrderResponseDTO 입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 주문에 필요한 회원, 옵션 정보가 잘못되었거나, 재고가 소진된 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<Page<OrderResponseDTO>> getOrders(Pageable pageable){
        return new ResponseEntity<>(orderService.getOrder(pageable), HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    @Operation(description = "클라이언트가 제출한 주문을 처리하고, 카카오톡으로 주문 메세지를 보냅니다. " +
            " 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.", tags = "Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문을 성공하였습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 보통 주문에 필요한 회원, 옵션 정보가 잘못되었거나, 재고가 소진된 경우입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class))),
            @ApiResponse(responseCode = "500", description = "서버에 의한 오류입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDTO.class)))})
    public ResponseEntity<?> order(@Parameter(hidden = true) @LoginMember MemberDTO memberDTO, @RequestBody OrderRequestDTO orderRequestDTO) {
        String accessToken = memberService.getMemberAccessToken(memberDTO.getEmail());

        optionService.subtractOptionQuantity(orderRequestDTO.optionId(), orderRequestDTO.quantity());

        Option option = optionService.getOption(orderRequestDTO.optionId());

        wishListService.removeWishListProduct(memberDTO, option.getProduct().getId());
        kakaoTokenService.sendMsgToMe(accessToken, option, orderRequestDTO.message());
        OrderResponseDTO orderResponseDTO = orderService.saveOrderHistory(orderRequestDTO);

        return new ResponseEntity<>(orderResponseDTO, HttpStatus.CREATED);
    }
}
