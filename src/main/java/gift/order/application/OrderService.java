package gift.order.application;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.kakao.client.KakaoClient;
import gift.member.application.MemberService;
import gift.member.entity.Member;
import gift.order.dao.OrderRepository;
import gift.order.dto.OrderRequest;
import gift.order.dto.OrderResponse;
import gift.order.util.OrderMapper;
import gift.product.application.OptionService;
import gift.product.entity.Option;
import gift.wishlist.application.WishesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OptionService optionService;
    private final WishesService wishesService;
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final KakaoClient kakaoClient;

    private static final Double POINT_ACCUMULATION_RATE = 0.05;

    public OrderService(OptionService optionService,
                        WishesService wishesService,
                        OrderRepository orderRepository,
                        MemberService memberService,
                        KakaoClient kakaoClient) {
        this.optionService = optionService;
        this.wishesService = wishesService;
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.kakaoClient = kakaoClient;
    }

    @Transactional
    public OrderResponse order(Long memberId, OrderRequest request) {
        Option option = optionService.getOptionByIdOrThrow(request.optionId());
        Member member = memberService.getMemberByIdOrThrow(memberId);
        int purchaseAmount = request.quantity() * option.getProduct()
                                                        .getPrice();

        processPayment(purchaseAmount, request.point());
        memberService.subtractMemberPoint(member, request.point());
        optionService.subtractQuantity(option, request.quantity());

        processMemberPointAccumulation(purchaseAmount, member);

        wishesService.removeWishIfPresent(memberId, option.getId());

        memberService.checkKakaoUserAndToken(member);
        kakaoClient.sendMessageToMe(
                member.getKakaoAccessToken(),
                request.message(),
                "/members/order/" + option.getId()
        );

        return OrderMapper.toResponseDto(
                orderRepository.save(OrderMapper.toEntity(request, option, member))
        );
    }

    private void processMemberPointAccumulation(int purchaseAmount,
                                                Member member) {
        int point = (int) (purchaseAmount * POINT_ACCUMULATION_RATE);
        memberService.saveMemberPoint(member, point);
    }

    private void processPayment(int purchaseAmount, int point) {
        if (point > purchaseAmount) {
            throw new CustomException(ErrorCode.ORDER_POINT_EXCEED_PURCHASE_AMOUNT);
        }

        // 결제 과정 생략
    }

    public Page<OrderResponse> getPagedOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderMapper::toResponseDto);
    }

}
