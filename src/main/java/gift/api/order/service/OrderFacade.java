package gift.api.order.service;

import gift.api.member.service.KakaoService;
import gift.api.member.service.MemberService;
import gift.api.option.domain.Option;
import gift.api.option.service.OptionService;
import gift.api.order.dto.OrderRequest;
import gift.api.order.dto.OrderResponse;
import gift.api.wishlist.service.WishService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderFacade {

    private final OrderService orderService;
    private final OptionService optionService;
    private final WishService wishService;
    private final KakaoService kakaoService;
    private final MemberService memberService;

    public OrderFacade(OrderService orderService, OptionService optionService,
        WishService wishService, KakaoService kakaoService, MemberService memberService) {
        this.orderService = orderService;
        this.optionService = optionService;
        this.wishService = wishService;
        this.kakaoService = kakaoService;
        this.memberService = memberService;
    }

    @Transactional
    public OrderResponse order(Long memberId, OrderRequest orderRequest) {
        Option option = optionService.findOptionById(orderRequest.optionId());
        int point = memberService.subtractPoint(memberId, orderRequest.point());
        optionService.subtractQuantity(orderRequest.optionId(), orderRequest.quantity());
        wishService.deleteIfExists(memberId, option.getProductId());
        kakaoService.sendMessage(memberId, orderService.createBody(orderRequest));
        return OrderResponse.of(
            orderService.save(memberService.findMemberById(memberId), option, orderRequest), point);
    }
}
