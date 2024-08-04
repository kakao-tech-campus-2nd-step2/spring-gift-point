package gift.service;

import gift.annotation.LoginMember;
import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenClient.order.OrderRequestDTO;
import gift.dto.betweenClient.order.OrderResponseDTO;
import gift.entity.Option;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class OrderService {

    private final KakaoTokenService kakaoTokenService;
    private final MemberService memberService;
    private final OptionService optionService;
    private final WishListService wishListService;
    private final OrderHistoryService orderHistoryService;

    public OrderService(KakaoTokenService kakaoTokenService, MemberService memberService,
            OptionService optionService, WishListService wishListService, OrderHistoryService orderHistoryService) {
        this.kakaoTokenService = kakaoTokenService;
        this.memberService = memberService;
        this.optionService = optionService;
        this.wishListService = wishListService;
        this.orderHistoryService = orderHistoryService;
    }
    public Page<OrderResponseDTO> getOrders(Pageable pageable){
        return orderHistoryService.getOrder(pageable);
    }

    @Transactional
    public OrderResponseDTO order(@Parameter(hidden = true) @LoginMember MemberDTO memberDTO, @RequestBody OrderRequestDTO orderRequestDTO) {
        String accessToken = memberService.getMemberAccessToken(memberDTO.getEmail());
        optionService.subtractOptionQuantity(orderRequestDTO.optionId(),
                orderRequestDTO.quantity());

        Option option = optionService.getOption(orderRequestDTO.optionId());
        wishListService.removeWishListProduct(memberDTO, option.getProduct().getId());

        if(!accessToken.isBlank())
            kakaoTokenService.sendMsgToMe(accessToken, option, orderRequestDTO.message());

        return orderHistoryService.saveOrderHistory(orderRequestDTO);
    }
}
