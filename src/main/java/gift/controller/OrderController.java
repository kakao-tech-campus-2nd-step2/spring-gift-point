package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.OrderRequestDTO;
import gift.dto.OrderResponseDTO;
import gift.model.Member;
import gift.service.KakaoService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders/{optionId}")
@Tag(name = "주문 관리 API", description = "주문 관리를 위한 API")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final KakaoService kakaoService;

    public OrderController(OrderService orderService, KakaoService kakaoService) {
        this.orderService = orderService;
        this.kakaoService = kakaoService;
    }

    @GetMapping
    @Operation(summary = "주문 폼 보기", description = "주문할 수 있는 폼으로 이동합니다.")
    public String showOrderForm(@PathVariable("optionId") Long optionId, Model model) {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO(optionId, 1L, "임시 메시지", null);
        model.addAttribute("orderRequestDTO", orderRequestDTO);
        return "order_form";
    }

    @PostMapping
    @Operation(summary = "주문하기", description = "주문을 합니다. 주문과 동시에 카카오톡 메시지를 보냅니다.")
    public String addOrder(@PathVariable("optionId") Long optionId,
        @RequestBody @Valid OrderRequestDTO orderRequestDTO, @LoginMember Member member) {
        if (member == null) {
            return "redirect:/members/login";
        }
        OrderResponseDTO orderResponseDTO = orderService.createOrder(orderRequestDTO,
            member.getEmail());
        String accessToken = orderRequestDTO.accessToken();
        try {
            kakaoService.sendKakaoMessage(accessToken, orderResponseDTO);
        } catch (Exception e) {
            logger.error("카카오톡 메시지 전송 실패");
        }
        return "redirect:/admin/products";
    }

}
