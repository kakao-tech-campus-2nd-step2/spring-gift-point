package gift.order.application;

import gift.auth.KakaoMessageSend;
import gift.auth.KakaoService;
import gift.exception.type.NotFoundException;
import gift.member.domain.Member;
import gift.member.domain.MemberRepository;
import gift.option.application.OptionService;
import gift.option.domain.Option;
import gift.option.domain.OptionRepository;
import gift.order.application.command.OrderCreateCommand;
import gift.order.domain.Order;
import gift.order.domain.OrderRepository;
import gift.wishlist.domain.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishlistRepository wishlistRepository;
    private final OptionService optionService;
    private final MemberRepository memberRepository;
    private final KakaoService kakaoService;

    public OrderService(
            OrderRepository orderRepository,
            OptionRepository optionRepository,
            OptionService optionService,
            WishlistRepository wishlistRepository,
            MemberRepository memberRepository,
            KakaoService kakaoService
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.optionService = optionService;
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.kakaoService = kakaoService;
    }

    @Transactional
    public void save(OrderCreateCommand command, Long memberId) {
        Option option = optionRepository.findById(command.optionId())
                .orElseThrow(() -> new NotFoundException("해당 옵션이 존재하지 않습니다."));

        // 여기서 두번 옵션 조회가 발생함.
        optionService.subtractOptionQuantity(command.toOptionSubtractQuantityCommand());

        wishlistRepository.findAllByMemberId(memberId)
                .stream()
                .filter(wishlist -> wishlist.getProduct().equals(option.getProduct()))
                .findAny()
                .ifPresent(wishlistRepository::delete);


        memberRepository.findById(memberId)
                .ifPresent(member -> sendOrderMessage(command, member));

        Order order = command.toOrder(option);

        orderRepository.save(order);
    }

    private void sendOrderMessage(OrderCreateCommand command, Member member) {
        kakaoService.sendOrderMessage(
                member.getKakaoId(),
                new KakaoMessageSend(command.message()),
                kakaoService.getValidAccessToken(member)
        );
    }
}
