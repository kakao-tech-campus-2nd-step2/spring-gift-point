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
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishListRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final WishListRepository wishListRepository;
    private final OptionRepository optionRepository;
    private final OptionService optionService;
    private final MessageService messageService;

    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository,
        WishListRepository wishListRepository, OptionRepository optionRepository, OptionService optionService,
        MessageService messageService) {
        this.orderRepository = orderRepository;
        this.wishListRepository = wishListRepository;
        this.memberRepository = memberRepository;
        this.optionRepository = optionRepository;
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
        Member member = memberRepository.findById(loginMember.id()).get();
        Option option = optionRepository.findById(orderRequest.optionId()).get();

        if (member.getPoint() < orderRequest.point()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 보유중인 포인트 보다 많은 포인트를 사용할 수 없습니다");
        }
        member.subPoint(orderRequest.point());
        optionService.subtractQuantity(orderRequest.optionId(), orderRequest.quantity());

        Order order = orderRepository.save(
            new Order(new Member(loginMember.id()), orderRequest));
        member.addPoint((long)(option.getProduct().getPrice() * 0.05));
        wishListRepository.findByMemberAndOption(new Member(loginMember.id()),
                new Option(orderRequest.optionId()))
            .ifPresent(wishListRepository::delete);

        if (member.getKakaoToken() != null) {
            KakaoMessageTemplate message = new KakaoMessageTemplate("text", "상품 구매가 완료되었습니다",
                new KakakoMessageLink("https://pnuece.pnu.app", "https://pnuece.pnu.app"), "바로기기");
            messageService.kakaoTalk(member.getKakaoToken(), message);
        }

        return order.toOrderResponse();
    }
}
