package gift.service;

import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenClient.order.OrderRequestDTO;
import gift.dto.betweenClient.order.OrderResponseDTO;
import gift.entity.Option;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.InternalServerExceptions.InternalServerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final double DISCOUNT_RATIO = 0.9;

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
    public OrderResponseDTO order(MemberDTO memberDTO, OrderRequestDTO orderRequestDTO) throws BadRequestException, InternalServerException {

        String accessToken = memberService.getMemberAccessToken(memberDTO.getEmail());
        Option option = optionService.getOption(orderRequestDTO.optionId());

        memberService.subtractPoint(memberDTO, Long.valueOf(discountFilter(option.getProduct().getPrice())));
        optionService.subtractOptionQuantity(orderRequestDTO.optionId(),
                orderRequestDTO.quantity());

        wishListService.removeWishListProduct(memberDTO, option.getProduct().getId());

        if(!accessToken.isBlank())
            kakaoTokenService.sendMsgToMe(accessToken, option, orderRequestDTO.message());

        return orderHistoryService.saveOrderHistory(orderRequestDTO);
    }

    private Integer discountFilter(Integer price){
        if(price >= 50000)
            price = (int) (price * DISCOUNT_RATIO);

        return price;
    }
}
