package gift.service;

import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Wish;
import gift.exception.BusinessException;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository, MemberRepository memberRepository, WishRepository wishRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;
    }

    public OrderResponseDto createOrder(Long memberId, OrderRequestDto request) {
        Option option = optionRepository.findById(request.getOptionId()).orElseThrow(() -> new BusinessException("해당 id에 대한 옵션이 없습니다.")) ;
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException("해당 id에 대한 사용자가 없습니다."));

        if (option.getQuantity() < request.getQuantity()) {
            throw new BusinessException("수량이 부족합니다.");
        }
        option.subtract(request.getQuantity());

        Optional<Wish> wish = wishRepository.findByMemberIdAndProductId(memberId, option.getProduct().getId());
        if (wish.isPresent()) {
            wishRepository.delete(wish.get());
        }

        Order order = new Order(request.getQuantity(), request.getMessage(), LocalDateTime.now(), option, member);
        return new OrderResponseDto(orderRepository.save(order));
    }
}