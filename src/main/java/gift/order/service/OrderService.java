package gift.order.service;

import gift.option.domain.Option;
import gift.order.dto.OrderResponse;
import gift.option.repository.OptionJpaRepository;
import gift.order.domain.Order;
import gift.order.dto.OrderRequest;
import gift.order.repository.OrderJPARepository;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OptionJpaRepository optionJpaRepository;
    private final WishService wishService;
    private final OrderJPARepository orderJPARepository;

    public OrderService(OptionJpaRepository optionJpaRepository, WishService wishService, OrderJPARepository orderJPARepository) {
        this.optionJpaRepository = optionJpaRepository;
        this.wishService = wishService;
        this.orderJPARepository = orderJPARepository;
    }

    public OrderResponse requestOrder(@Valid OrderRequest orderRequest) {
        // 주문할 때 상품 옵션과 수량을 받아온다.
        Long request_optionId = orderRequest.getOptionId();
        Long request_quantity = orderRequest.getQuantity();
        String request_message = "Please handle with care";

        Option option = optionJpaRepository.findById(request_optionId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 다음 id의 옵션은 존재하지 않음 : " + request_optionId));

        if (request_quantity > option.getQuantity()) {
            throw new IllegalArgumentException("[ERROR] 주문 수량이 재고 수량을 초과함");
        }

        // 주문 로직
        option.subtract(request_quantity); // 주문 수량만큼 옵션 재고 감소
        Long productId = option.getProduct().getId();
        if (wishService.getWishByProductId(productId) != null) {
            wishService.deleteWish(productId); // 위시리스트에 해당 상품이 있으면 삭제
        }

        // 주문 내역 저장
        orderJPARepository.save(new Order(orderRequest.getOptionId(), orderRequest.getQuantity(), LocalDateTime.now(), request_message));
        Long orderId = orderJPARepository.findByOptionId(request_optionId).getId();

        return new OrderResponse(orderId, request_optionId, request_quantity, LocalDateTime.now(), request_message);
    }
}
