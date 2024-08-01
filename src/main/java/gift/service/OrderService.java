package gift.service;

import gift.config.KakaoUserClinet;
import gift.dto.memberDto.MemberDto;
import gift.dto.orderDto.OrderRequestDto;
import gift.dto.orderDto.OrderResponseDto;
import gift.dto.orderDto.OrderResponseMapper;
import gift.exception.ValueNotFoundException;
import gift.model.member.Member;
import gift.model.order.Order;
import gift.model.product.Option;
import gift.model.product.Product;
import gift.model.wish.Wish;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final KakaoUserClinet kakaoUserClinet;

    private final OrderResponseMapper orderResponseMapper;

    public OrderService(KakaoUserClinet kakaoUserClinet,OptionRepository optionRepository,WishRepository wishRepository,OrderRepository orderRepository, MemberRepository memberRepository,OrderResponseMapper orderResponseMapper) {
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.kakaoUserClinet = kakaoUserClinet;
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.orderResponseMapper = orderResponseMapper;
    }

    public OrderResponseDto requestOrder(OrderRequestDto orderRequestDto, String kakaoAuthorization, MemberDto memberDto) {
        Option option = optionRepository.findById(orderRequestDto.optionId()).
                orElseThrow(() -> new ValueNotFoundException("Option not exists in Database"));

        option.updateAmount(orderRequestDto.quantity());
        optionRepository.save(option);

        Product product = option.getProduct();
        Optional<Wish> wish = wishRepository.findByProductId(product.getId());
        if (wish.isPresent()) {
            wishRepository.delete(wish.get());
        }
        int resultCode = kakaoUserClinet.sendOrderConfirmationMessage(orderRequestDto.message(), kakaoAuthorization).block();
        if (resultCode != 0) {
            throw new RuntimeException("Kakao Server Error");
        }
        Member member = memberRepository.findByEmail(memberDto.email()).
                orElseThrow(() -> new ValueNotFoundException("Option not exists in Database"));
        Order order = new Order(option,member,orderRequestDto.quantity(),orderRequestDto.message());
        Order savedOrder = orderRepository.save(order);

        return orderResponseMapper.toOrderResponseDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrderList(MemberDto memberDto, int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Member member = memberRepository.findByEmail(memberDto.email()).
                orElseThrow(() -> new ValueNotFoundException("Option not exists in Database"));

        Page<Order> orderPage = orderRepository.findByMemberId(member.getId(), pageable);

        List<OrderResponseDto> orderListResponseDtos = orderPage.stream()
                .map(orderResponseMapper::toOrderResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(orderListResponseDtos, pageable, orderPage.getTotalElements());
    }
}
