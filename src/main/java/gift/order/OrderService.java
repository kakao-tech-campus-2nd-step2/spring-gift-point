package gift.order;

import gift.api.kakaoMessage.KakaoMessageClient;
import gift.common.auth.LoginMemberDto;
import gift.common.exception.MemberException;
import gift.common.exception.OptionException;
import gift.member.MemberErrorCode;
import gift.member.MemberRepository;
import gift.member.model.Member;
import gift.member.oauth.OauthTokenRepository;
import gift.member.oauth.model.OauthToken;
import gift.option.OptionErrorCode;
import gift.option.OptionRepository;
import gift.option.model.Option;
import gift.wish.WishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final OauthTokenRepository oauthTokenRepository;
    private final WishRepository wishRepository;
    private final KakaoMessageClient kakaoMessageClient;

    public OrderService(MemberRepository memberRepository, OrderRepository orderRepository,
        OptionRepository optionRepository,
        OauthTokenRepository oauthTokenRepository, WishRepository wishRepository,
        KakaoMessageClient kakaoMessageClient) {
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.oauthTokenRepository = oauthTokenRepository;
        this.wishRepository = wishRepository;
        this.kakaoMessageClient = kakaoMessageClient;
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest, LoginMemberDto loginMemberDto)
        throws OptionException, MemberException {
        Member member = memberRepository.findById(loginMemberDto.getId()).orElseThrow();
        member.usePoint(orderRequest.point());

        Option option = optionRepository.findById(orderRequest.optionId())
            .orElseThrow(() -> new OptionException(OptionErrorCode.NOT_FOUND));
        if (wishRepository.existsByMemberIdAndProductId(loginMemberDto.getId(),
            option.getProduct().getId())) {
            wishRepository.deleteByMemberIdAndProductId(loginMemberDto.getId(),
                option.getProduct().getId());
        }

        option.subtract(orderRequest.quantity());
        Order order = new Order(loginMemberDto.toEntity(), option, orderRequest.quantity(),
            orderRequest.message());
        orderRepository.save(order);

        OauthToken oauthToken = oauthTokenRepository.findByMemberId(loginMemberDto.getId())
            .orElseThrow();
        kakaoMessageClient.sendOrderMessage(oauthToken.getAccessToken(), order);
        return OrderResponse.from(order);
    }
}
