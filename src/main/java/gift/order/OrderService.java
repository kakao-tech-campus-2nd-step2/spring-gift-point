package gift.order;

import gift.api.kakaoMessage.KakaoMessageClient;
import gift.common.auth.LoginMemberDto;
import gift.common.exception.MemberException;
import gift.common.exception.OptionException;
import gift.member.MemberRepository;
import gift.member.model.Member;
import gift.member.oauth.OauthTokenRepository;
import gift.member.oauth.model.OauthToken;
import gift.option.OptionErrorCode;
import gift.option.OptionRepository;
import gift.option.model.Option;
import gift.order.model.Order;
import gift.order.model.OrderRequest;
import gift.order.model.OrderResponse;
import gift.product.model.Product;
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
        Option option = optionRepository.findById(orderRequest.optionId())
            .orElseThrow(() -> new OptionException(OptionErrorCode.NOT_FOUND));
        deleteWish(loginMemberDto, option);
        option.subtract(orderRequest.quantity());
        Product product = option.getProduct();
        Order order = saveOrder(loginMemberDto, orderRequest, option, product);
        updateMemberPoint(loginMemberDto, orderRequest, product);
        sendOrderMessage(loginMemberDto, order);
        return OrderResponse.from(order);
    }

    private Order saveOrder( LoginMemberDto loginMemberDto,OrderRequest orderRequest, Option option,
        Product product) {
        Order order = new Order(loginMemberDto.toEntity(), option, orderRequest.quantity(), orderRequest.message(), product.calTotalPrice(
            orderRequest.quantity()), orderRequest.point(), calAccumulatedPoint(orderRequest, product));
        orderRepository.save(order);
        return order;
    }

    private int calAccumulatedPoint(OrderRequest orderRequest, Product product) {
        return (product.calTotalPrice(orderRequest.quantity()) - orderRequest.point()) / 10;
    }

    private void updateMemberPoint(LoginMemberDto loginMemberDto, OrderRequest orderRequest, Product product) throws MemberException {
        Member member = memberRepository.findById(loginMemberDto.getId()).orElseThrow();
        member.usePoint(orderRequest.point());
        member.accumulatePoint(calAccumulatedPoint(orderRequest, product));
    }

    private void sendOrderMessage(LoginMemberDto loginMemberDto, Order order) {
        OauthToken oauthToken = oauthTokenRepository.findByMemberId(loginMemberDto.getId())
            .orElseThrow();
        kakaoMessageClient.sendOrderMessage(oauthToken.getAccessToken(), order);
    }

    private void deleteWish(LoginMemberDto loginMemberDto, Option option) {
        if (wishRepository.existsByMemberIdAndProductId(loginMemberDto.getId(),
            option.getProduct().getId())) {
            wishRepository.deleteByMemberIdAndProductId(loginMemberDto.getId(),
                option.getProduct().getId());
        }
    }
}
