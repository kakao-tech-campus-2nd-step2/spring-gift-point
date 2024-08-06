package gift.service;

import gift.domain.member.Member;
import gift.domain.member.MemberId;
import gift.domain.message.KakakoMessageLink;
import gift.domain.message.KakaoMessageTemplate;
import gift.domain.option.Option;
import gift.domain.order.Order;
import gift.domain.order.OrderRequest;
import gift.domain.order.OrderResponse;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.repository.WishListRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final WishListRepository wishListRepository;
    private final OptionService optionService;
    private final MessageService messageService;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository,
        WishListRepository wishListRepository, OptionService optionService,
        MessageService messageService) {
        this.orderRepository = orderRepository;
        this.wishListRepository = wishListRepository;
        this.memberRepository = memberRepository;
        this.optionService = optionService;
        this.messageService = messageService;
    }

    public List<OrderResponse> findByMemberId(Long memberId) {
        return orderRepository.findByMember(new Member(memberId))
            .stream()
            .map(Order::toOrderResponse)
            .toList();
    }

    @Transactional
    public OrderResponse create(MemberId loginMember, OrderRequest orderRequest) {
        wishListRepository.findByMemberAndOption(new Member(loginMember.id()),
                new Option(orderRequest.optionId()))
            .ifPresent(wishListRepository::delete);

        optionService.subtractQuantity(orderRequest.optionId(), orderRequest.quantity());

        Order order = orderRepository.save(
            new Order(new Member(loginMember.id()), orderRequest));

        KakaoMessageTemplate message = new KakaoMessageTemplate("text", "상품 구매가 완료되었습니다",
            new KakakoMessageLink("http://naver.com", "http://google.com"), "바로기기");

        memberRepository.findById(loginMember.id())
            .ifPresent(member -> messageService.kakaoTalk(member.getKakaoToken(), message));

        return order.toOrderResponse();
    }
}
