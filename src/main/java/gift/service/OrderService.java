package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Order;
import gift.exception.LoginException;
import gift.exception.OptionException;
import gift.exception.WishException;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;
    private final WishService wishService;
    private final KakaoService kakaoService;


    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository,
        OptionRepository optionRepository, WishService wishService, KakaoService kakaoService) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.optionRepository = optionRepository;
        this.wishService = wishService;
        this.kakaoService = kakaoService;
    }

    public OrderResponseDto addOrder(String email, OrderRequestDto orderRequestDto)
        throws WishException {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new LoginException("올바르지 않은 사용자 입니다."));
        Option option = optionRepository.findById(orderRequestDto.getOptionId())
            .orElseThrow(() -> new OptionException("올바르지 않은 옵션입니다."));

        if (option.getQuantity() < orderRequestDto.getQuantity()) {
            throw new OptionException("수량이 부족합니다.");
        }
        option.setQuantity(option.getQuantity() - orderRequestDto.getQuantity());
        optionRepository.save(option);
        wishService.deleteWishlist(member.getId(), option.getProduct().getId());
        Order order = new Order(orderRequestDto.getQuantity(), LocalDateTime.now(),
            orderRequestDto.getMessage(), option, member);
        Order savedOrder = orderRepository.save(order);

        try {
            kakaoService.kakaoTalkToMe(member.getAccessToken(), savedOrder);
        }catch (JsonProcessingException e) {
            throw new OptionException("잘못된 요청");
        }
        return new OrderResponseDto(savedOrder.getId(), savedOrder.getOption().getId(),
            savedOrder.getQuantity(),
            savedOrder.getOrderDateTime(), savedOrder.getMessage());
    }

    public Page<Order> findByMemberId(Long memberId, Pageable pageable) {
        return orderRepository.findByMemberId(memberId, pageable);
    }

}
