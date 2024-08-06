package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.DTO.Order.OrderLogResponse;
import gift.DTO.Order.OrderRequest;
import gift.DTO.Order.OrderResponse;
import gift.DTO.Product.ProductResponse;
import gift.DTO.User.UserResponse;
import gift.domain.*;
import gift.exception.LogicalException;
import gift.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final KakaoService kakaoUserService;

    public OrderService(
            OrderRepository orderRepository, OptionRepository optionRepository, WishListRepository wishListRepository,
            KakaoService kakaoUserService, ProductRepository productRepository, UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishListRepository = wishListRepository;
        this.kakaoUserService = kakaoUserService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    /*
     * 주문 정보를 저장하는 로직
     */
    @Transactional
    public OrderResponse save(OrderRequest orderRequest){
        Order order = new Order(
                orderRequest.getOptionId(),
                orderRequest.getQuantity(),
                orderRequest.getMessage()
        );

        Order savedOrder = orderRepository.save(order);
        return new OrderResponse(savedOrder);
    }
    /*
     * 주문 전반을 진행하는 로직
     * 수량 감소 및 위시 리스트에서 삭제 -> 메세지 전송 -> 저장 -> 주문 정보 전달
     */
    @Transactional
    public OrderResponse order(
            OrderRequest orderRequest, UserResponse userResponse) throws JsonProcessingException
    {
        // 카카오 유저 검사
        if(userResponse.getToken() == null)
            throw new NoSuchFieldError("카카오 유저만 구매할 수 있습니다!");
        // 상품 수량 로직
        Option option = optionRepository.findById(orderRequest.getOptionId()).orElseThrow(NoSuchFieldError::new);
        int before = option.getQuantity();
        option.subtract(orderRequest.getQuantity());
        int after = option.getQuantity();
        if(before == after){
            throw new LogicalException("수량보다 많은 수는 주문할 수 없습니다!");
        }
        // 포인트 사용 로직
        User user = userRepository.findById(userResponse.getId()).orElseThrow(NoSuchFieldError::new);
        before = user.getPoint();
        user.usePoint(orderRequest.getPoint());
        after = user.getPoint();
        if(before == after){
            throw new LogicalException("보유한 포인트보다 많은 포인트는 사용할 수 없습니다!");
        }
        // 위시 리스트 삭제 로직
        Product product = productRepository.findById(orderRequest.getProductId()).orElseThrow(NoSuchFieldError::new);
        ProductResponse productResponse = new ProductResponse(product);
        if(wishListRepository.existsByUserIdAndProductId(userResponse.getId(), productResponse.getId())){
            WishProduct wish = wishListRepository.findByUserIdAndProductId(userResponse.getId(), productResponse.getId());
            wishListRepository.deleteById(wish.getId());
        }
        // 포인트 추가 로직
        user.addPoint(orderRequest.getQuantity() * product.getPrice() * 10 / 100);
        // 구매 관련 메세지 보내기
        kakaoUserService.messageToMe(
                userResponse.getToken(), productResponse, option.getName(), orderRequest
        );
        // 구매 완료, 주문 기록 저장
        return save(orderRequest);
    }

    /*
     * 주문 기록을 정렬하여 페이지 형식으로 조회
     */
    public Page<OrderLogResponse> findAll(Pageable pageable){
        List<OrderLogResponse> answer = new ArrayList<>();

        Page<Order> orders = orderRepository.findAll(pageable);
        List<Order> orderContent = orders.getContent();
        for (Order order : orderContent) {
            Option option = optionRepository.findById(order.getOptionId()).orElseThrow(NoSuchFieldError::new);
            Product product = option.getProduct();
            OrderLogResponse orderLogResponse = new OrderLogResponse(
                    new ProductResponse(product), new OrderResponse(order)
            );
            answer.add(orderLogResponse);
        }
        return new PageImpl<>(answer, pageable, answer.size());
    }
}
