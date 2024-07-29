package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.DTO.Order.OrderRequest;
import gift.DTO.Order.OrderResponse;
import gift.DTO.Product.ProductResponse;
import gift.DTO.User.UserResponse;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.domain.WishProduct;
import gift.exception.LogicalException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.ProductRepository;
import gift.repository.WishListRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final KakaoService kakaoUserService;

    public OrderService(
            OrderRepository orderRepository, OptionRepository optionRepository, WishListRepository wishListRepository,
            KakaoService kakaoUserService, ProductRepository productRepository
    ) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishListRepository = wishListRepository;
        this.kakaoUserService = kakaoUserService;
        this.productRepository = productRepository;
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
            OrderRequest orderRequest, UserResponse userResponse, Long productId) throws JsonProcessingException
    {
        if(userResponse.getToken() == null)
            throw new NoSuchFieldError("카카오 유저만 구매할 수 있습니다!");

        Option option = optionRepository.findById(orderRequest.getOptionId()).orElseThrow(NoSuchFieldError::new);
        Long before = option.getQuantity();
        option.subtract(orderRequest.getQuantity());
        Long after = option.getQuantity();

        if(before.equals(after)){
            throw new LogicalException("수량보다 많은 수는 주문할 수 없습니다!");
        }

        Product product = productRepository.findById(productId).orElseThrow(NoSuchFieldError::new);
        ProductResponse productResponse = new ProductResponse(product);

        if(wishListRepository.existsByUserIdAndProductId(userResponse.getId(), productResponse.getId())){
            WishProduct wish = wishListRepository.findByUserIdAndProductId(userResponse.getId(), productResponse.getId());
            wishListRepository.deleteById(wish.getId());
        }

        kakaoUserService.messageToMe(
                userResponse.getToken(), productResponse, option.getName(), orderRequest
        );

        return save(orderRequest);
    }
}
