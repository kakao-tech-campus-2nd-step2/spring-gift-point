package gift.service;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.ServiceException;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        MemberRepository memberRepository, WishRepository wishRepository,
        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public Page<OrderResponseDto> findAll(Long memberId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByMemberId(memberId, pageable);

        List<OrderResponseDto> orderResponseDtoList = orderPage.stream().map(OrderResponseDto::new)
            .collect(Collectors.toList());

        return new PageImpl<>(orderResponseDtoList, pageable, orderPage.getTotalElements());
    }

    public OrderResponseDto createOrder(Long memberId, OrderRequestDto request) {
        Option option = optionRepository.findById(request.getOptionId())
            .orElseThrow(() -> new ServiceException("옵션을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        Product product = productRepository.findById(option.getProduct().getId())
            .orElseThrow(() -> new ServiceException("상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new ServiceException("유저 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (request.getQuantity() < 1) {
            throw new ServiceException("유효하지 않은 수량입니다.", HttpStatus.BAD_REQUEST);
        }

        if (option.getQuantity() < request.getQuantity()) {
            throw new ServiceException("수량은 0보다 작을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        option.subtract(request.getQuantity());

        Optional<Wish> wish = wishRepository.findByMemberIdAndProductId(memberId,
            option.getProduct().getId());
        if (wish.isPresent()) {
            wishRepository.delete(wish.get());
        }

        Order order = new Order(request.getQuantity(), request.getMessage(), LocalDateTime.now(),
            option, member);
        return new OrderResponseDto(orderRepository.save(order));
    }
}