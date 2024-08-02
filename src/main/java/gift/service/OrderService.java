package gift.service;

import gift.api.OrderRequest;
import gift.api.OrderResponse;
import gift.converter.OrderConverter;
import gift.dto.OrderDTO;
import gift.dto.PageRequestDTO;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OptionService optionService;
    private final UserService userService;
    private final WishListService wishListService;
    private final KakaoService kakaoService;
    private final KakaoMessageService kakaoMessageService;
    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OptionService optionService, UserService userService,
        WishListService wishListService, KakaoService kakaoService, KakaoMessageService kakaoMessageService, OrderRepository orderRepository, ProductService productService) {
        this.optionService = optionService;
        this.userService = userService;
        this.wishListService = wishListService;
        this.kakaoService = kakaoService;
        this.kakaoMessageService = kakaoMessageService;
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    public OrderResponse createOrder(String authorization, OrderDTO orderDTO) {
        // Option 수량 차감
        boolean updated = optionService.decreaseOptionQuantity(orderDTO.getOptionId(), orderDTO.getQuantity());

        if (!updated) {
            throw new IllegalArgumentException("Insufficient product quantity.");
        }

        String token = authorization.replace("Bearer ", "");
        // access token에서 이메일 추출
        String email = kakaoService.getUserEmail(token);

        Long productId = optionService.getProductIdByOptionId(orderDTO.getOptionId());

        int amount = (int) productService.getProductPriceById(productId)/10;

        //email로 point객체찾고 포인트 수량 알아내고, product가격만큼 포인트 제외시킴(빠진 포인트값 기억)
        int remainingPoints = userService.deductPoints(email, amount);

        LocalDateTime now = LocalDateTime.now();
        OrderRequest order = new OrderRequest(
            orderDTO.getOptionId(),
            orderDTO.getQuantity(),
            orderDTO.getMessage(),
            now
        );
        orderRepository.save(order);
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setOrderDateTime(now);
        //여기에 orderDTO 메시지 정해서 얼마 할인됐는지 추가(포인트값)

        // 카카오톡 메시지 전송
        OrderResponse orderResponse = kakaoMessageService.sendKakaoMessage(token, orderDTO);






        if (wishListService.isProductInWishList(email, productId)){
            wishListService.removeProductFromWishList(email, productId);
        }

        return orderResponse;
    }
    public Page<OrderDTO> getOrderList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.toPageRequest();
        Page<OrderRequest> orders = orderRepository.findAll(pageable);
        return orders.map(OrderConverter::convertToDTO);
    }
}