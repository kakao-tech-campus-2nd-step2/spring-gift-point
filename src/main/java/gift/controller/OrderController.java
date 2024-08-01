package gift.controller;

import gift.dto.ApiResponse;
import gift.model.Order;
import gift.model.Option;
import gift.model.Member;
import gift.repository.OptionRepository;
import gift.repository.WishRepository;
import gift.repository.MemberRepository;
import gift.service.OrderService;
import gift.service.KakaoMessageService;
import gift.service.KakaoAuthService;
import gift.service.impl.KakaoMessageServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Tag(name = "Order API", description = "주문 관련 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final KakaoMessageService kakaoMessageService;
    private final KakaoAuthService kakaoAuthService;

    public OrderController(OrderService orderService, OptionRepository optionRepository, WishRepository wishRepository, MemberRepository memberRepository, KakaoAuthService kakaoAuthService) {
        this.orderService = orderService;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.kakaoMessageService = new KakaoMessageServiceImpl();
        this.kakaoAuthService = kakaoAuthService;
    }

    @Operation(summary = "주문 생성", description = "주문을 생성하고 결과를 반환합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(
        @Parameter(description = "카카오 인증 토큰", required = true) @RequestHeader("Authorization") String kakaoToken,
        @Parameter(description = "주문 정보", required = true) @RequestBody Order order) {
        try {
            String accessToken = kakaoToken.replace("Bearer ", "");
            Option option = optionRepository.findById(order.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));
            order.setOption(option);

            String email = kakaoAuthService.getUserInfo(accessToken).getEmail();
            Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member email"));

            Order createdOrder = orderService.createOrder(order, kakaoToken);
            option.subtractQuantity(order.getQuantity());
            optionRepository.save(option);

            wishRepository.deleteByMemberAndProduct(member, option.getProduct());

            kakaoMessageService.sendMessage(kakaoToken, "주문 내역: " + createdOrder.toString());

            return ResponseEntity.status(201).body(new ApiResponse<>(true, "Order created successfully", createdOrder, null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to create order", null, e.getMessage()));
        }
    }

    @Operation(summary = "카카오 메시지 전송", description = "카카오 메시지를 발송합니다.")
    @PostMapping("/sendMessage")
    public Mono<ResponseEntity<String>> sendMessage(
        @Parameter(description = "카카오 인증 토큰", required = true) @RequestHeader("Authorization") String kakaoToken,
        @Parameter(description = "메시지 내용", required = true) @RequestBody Map<String, String> requestBody) {
        String message = requestBody.get("message");
        return kakaoMessageService.sendMessage(kakaoToken, message);
    }
}
