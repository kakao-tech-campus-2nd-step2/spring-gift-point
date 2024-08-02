package gift.order.service;

import gift.option.domain.Option;
import gift.order.domain.Order;
import gift.order.dto.OrderResponse;
import gift.option.repository.OptionJpaRepository;
import gift.order.dto.OrderRequest;
import gift.order.repository.OrderJPARepository;
import gift.wish.service.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 주문하기 로직
    @Transactional
    public OrderResponse requestOrder(@Valid OrderRequest orderRequest) {
        // 포인트 있는지 확인


        // 주문할 때 상품 옵션과 수량을 받아온다.
        Long request_optionId = orderRequest.getOptionId();
        Long request_quantity = orderRequest.getQuantity();
        String request_message = orderRequest.getMessage();

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

        // 사용자 포인트를 저장


        return new OrderResponse(orderId, request_optionId, request_quantity, LocalDateTime.now(), request_message);
    }

    // 주문 목록 조회 (페이지네이션 적용)
    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrderByPage(int page, int size, String sortBy, String direction) {
        // validation
        if (page < 0 || size < 0) {
            throw new IllegalArgumentException("Invalid page or size");
        }

        // sorting
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);

        // paging
        Pageable pageable = PageRequest.of(page, size, sort); // Pageable 객체 생성
        Page<Order> orderPage = orderJPARepository.findAll(pageable); // Page<Order> 타입의 객체를 생성

        // Order 엔티티를 OrderResponse로 변환하고 Page 객체로 반환
        return orderPage.map(order -> new OrderResponse(
                order.getId(),
                order.getOptionId(),
                order.getQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        ));
    }
}
