package gift.order.application;

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
        Option option = optionService.getOptionById(request.optionId());
        optionService.subtractQuantity(option, request.quantity());

        Long productId = option.getProductId();
        wishesService.removeWishIfPresent(memberId, productId);

        Member member = memberService.getMemberByIdOrThrow(memberId);
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

    public Page<OrderResponse> getPagedOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderMapper::toResponseDto);
    }

}
