package gift.service;

import gift.LoginType;
import gift.domain.*;
import gift.dto.LoginMember;
import gift.dto.request.OrderRequest;
import gift.dto.response.OrderListResponse;
import gift.dto.response.OrderResponse;
import gift.dto.response.PointResponse;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static gift.exception.ErrorCode.DATA_NOT_FOUND;

@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final KaKaoService kaKaoService;

    public OrderService(OptionRepository optionRepository, WishRepository wishRepository, MemberRepository memberRepository, OrderRepository orderRepository, KaKaoService kaKaoService) {
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
        this.kaKaoService = kaKaoService;
    }

    @Transactional
    public OrderResponse order(LoginMember loginMember, OrderRequest orderRequest) {
        Option option = optionRepository.findById(orderRequest.optionId()).orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        Member member = memberRepository.findMemberById(loginMember.getId()).get();

        option.subtract(orderRequest.quantity());
        deleteFromWishList(loginMember.getId(), option.getProduct());
        member.deductPoint(orderRequest.point());
        member.savePoint(pointToSave(option.getProduct(), orderRequest.quantity()));
        Order savedOrder = orderRepository.save(new Order(option, member, orderRequest));
        sendKaKaoTalkToMe(member, orderRequest.message());

        return new OrderResponse(savedOrder);
    }

    private void deleteFromWishList(Long memberId, Product product) {
        Optional<Wish> wish = wishRepository.findWishByMember_IdAndProduct(memberId, product);
        wish.ifPresent(wishRepository::delete);
    }

    public Page<OrderListResponse> getOrderList(LoginMember loginMember, Pageable pageable) {
        Member member = memberRepository.findMemberById(loginMember.getId()).get();
        Page<Order> orders = orderRepository.findAllByMember(member, pageable);
        return orders.map(order -> new OrderListResponse(order, order.getOption().getProduct()));
    }

    public PointResponse getMemberPoint(Long memberId) {
        Member member = memberRepository.findMemberById(memberId).get();
        return new PointResponse(member.getPoint());
    }

    private int pointToSave(Product product, Integer quantity) {
        int totalPrice = product.getPrice() * quantity;
        return (int) (totalPrice * 0.1);
    }

    private void sendKaKaoTalkToMe(Member member, String message) {
        if (member.getLoginType().equals(LoginType.KAKAO)) {
            kaKaoService.sendMessage(message, member.getKakaoAccessToken());
        }
    }
}
