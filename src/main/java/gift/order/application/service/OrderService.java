package gift.order.application.service;

import gift.auth.KakaoClient;
import gift.exception.NotFoundMember;
import gift.exception.NotFoundOption;
import gift.exception.NotFoundOrder;
import gift.member.persistence.Member;
import gift.member.persistence.MemberRepository;
import gift.member.application.dto.RegisterRequestDto;
import gift.option.persistence.Option;
import gift.option.persistence.OptionRepository;
import gift.option.application.service.OptionService;
import gift.order.application.dto.OrderRequestDto;
import gift.order.application.dto.OrderResponseDto;
import gift.order.persistence.Order;
import gift.order.persistence.OrderRepository;
import gift.wishlist.persistence.WishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final KakaoClient kakaoClient;
    private final OptionService optionService;
    private final WishlistRepository wishlistRepository;

    public OrderService(
        OrderRepository orderRepository,
        OptionRepository optionRepository,
        MemberRepository memberRepository,
        KakaoClient kakaoClient,
        OptionService optionService,
        WishlistRepository wishlistRepository
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.kakaoClient = kakaoClient;
        this.optionService = optionService;
        this.wishlistRepository = wishlistRepository;
    }

    public OrderResponseDto createOrder(Member member, OrderRequestDto orderRequestDto)
        throws NotFoundMember {
        Option option = optionRepository.findById(orderRequestDto.optionId())
            .orElseThrow(() -> new NotFoundOption("해당 옵션을 찾을 수 없습니다"));

        option.substract(orderRequestDto.quantity());

        Order order = new Order(option, orderRequestDto.quantity(), orderRequestDto.message(), member);

        removeFromWishlistIfExists(member.getId(), order.getOption().getId());

        orderRepository.save(order);

        member.addPoint(option.getProduct().getPrice());
        member.usePoint(orderRequestDto.point());

        return new OrderResponseDto(
            order.getId(),
            order.getOption().getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }

    public Page<OrderResponseDto> getOrders(Member member, Pageable pageable)
        throws NotFoundMember {
        Member existMember = memberRepository.findByEmail(member.getEmail())
            .orElseThrow(() -> new NotFoundMember("회원정보가 올바르지 않습니다."));

        Page<Order>  orders = orderRepository.findAllByMemberId(member.getId(), pageable);

        return orders.map(order -> new OrderResponseDto(
            order.getId(),
            order.getOption().getId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        ));
    }

    public void deleteOrder(RegisterRequestDto memberRequestDto, Long orderId) throws NotFoundMember {
        Member member = memberRepository.findByEmail(memberRequestDto.email())
            .orElseThrow(() -> new NotFoundMember("회원정보가 올바르지 않습니다."));

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundOrder("유효하지 않은 주문입니다."));

        if (!order.getMember().getId().equals(member.getId())) {
            throw new NotFoundMember("유효하지 않은 주문입니다.");
        }

        orderRepository.delete(order);
    }

    public void removeFromWishlistIfExists(Long memberId, Long optionId) {
        Option option = optionRepository.findById(optionId)
            .orElseThrow(() -> new NotFoundOption("유효하지 않은 옵션입니다."));
        Long productId = option.getProduct().getId();

        if (wishlistRepository.existsByProductIdAndMemberId(productId, memberId)) {
            wishlistRepository.deleteByMemberIdAndProductId(memberId, productId);
        }
    }

}
