package gift.service;

import gift.dto.OrderRequestDto;
import gift.model.Member;
import gift.model.Option;
import gift.model.Order;
import gift.model.Product;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishlistRepository wishlistRepository;
    private final KakaoMessageService kakaoMessageService;
    private final MemberRepository memberRepository;

    public OrderService(OrderRepository orderRepository, OptionService optionService,
                        WishlistRepository wishlistRepository, KakaoMessageService kakaoMessageService, MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishlistRepository = wishlistRepository;
        this.kakaoMessageService = kakaoMessageService;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Order createOrder(OrderRequestDto orderRequestDto, Long memberId) {
        Option option = optionService.findOptionById(orderRequestDto.getOptionId());

        if (option.getQuantity() < orderRequestDto.getQuantity()) {
            throw new IllegalArgumentException("주문 수량이 재고 수량보다 많습니다.");
        }
        optionService.decreaseOptionQuantity(orderRequestDto.getOptionId(), orderRequestDto.getQuantity());

        Order order = new Order(option, orderRequestDto.getQuantity(), orderRequestDto.getMessage(), orderRequestDto.getPoints());
        orderRepository.save(order);
        Product product = optionService.findProductByOptionId(option.getId());

        wishlistRepository.deleteByMemberIdAndProductId(memberId, product.getId());
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원입니다."));
        Member updatedMember = new Member(member.getId(), member.getEmail(), member.getPassword(), member.getActiveToken(), (int) (orderRequestDto.getPoints() * 0.5));
        memberRepository.save(updatedMember);

        kakaoMessageService.sendMessageToKakao(order, memberId);
        return order;
    }

    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
