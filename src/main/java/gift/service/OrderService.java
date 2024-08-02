package gift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gift.dto.OrderDto;
import gift.dto.request.MessageRequest;
import gift.dto.request.OrderRequest;
import gift.dto.response.GetOrdersResponse;
import gift.dto.response.MessageResponse;
import gift.dto.response.OrderResponse;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import gift.repository.OrderRepository;
import gift.util.JwtUtil;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private OptionService optionService;
    private WishListService wishListService;
    private KakaoApiService kakaoApiService;
    private KakaoTokenService kakaoTokenService;
    private MemberRepository memberRepository;
    private JwtUtil jwtUtil;

    public OrderService(OrderRepository orderRepository, OptionService optionService, WishListService wishListService, KakaoTokenService kakaoTokenService, MemberRepository memberRepository, JwtUtil jwtUtil){
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishListService = wishListService;
        this.kakaoTokenService = kakaoTokenService;
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    public GetOrdersResponse getOrders(String token){

        long memberId = (long)jwtUtil.extractAllClaims(token).get("id");
        memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException("Member with token not exist", HttpStatus.UNAUTHORIZED, -40101));
        
        List<Order> orders = orderRepository.findByMemberId(memberId);
        return new GetOrdersResponse(orders.stream().map(OrderDto::fromEntity).toList());
    }

    @Transactional
    public OrderResponse makeOrder(String token, OrderRequest orderRequest){

        try {
            OrderResponse orderResponse = orderOption(token, orderRequest);
            deleteWishListByOrder(token, orderResponse.getOrder().getId());
            sendKakaoMessage(token, orderResponse);
            return orderResponse;
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getHttpStatus(), e.getCode());
        } catch (Exception e) {
            throw new CustomException("unexpected exception during makeing order", HttpStatus.INTERNAL_SERVER_ERROR, -50000);
        }
            
    }
    
    public OrderResponse orderOption(String token, OrderRequest orderRequest){

        long memberId = (long)jwtUtil.extractAllClaims(token).get("id");
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException("Member with token not exist", HttpStatus.UNAUTHORIZED, -40101));
        Option option = optionService.subtractQuantity(orderRequest.getOptionId(), orderRequest.getQuantity());

        Order order = new Order(member, option, orderRequest.getQuantity(), orderRequest.getMessage(), LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        
        OrderDto orderDto = new OrderDto(savedOrder.getId(), savedOrder.getOption().getId(), savedOrder.getQuantity(), savedOrder.getOrderTime(), savedOrder.getMessage());
        return new OrderResponse(orderDto);
    }

    public void deleteWishListByOrder(String token, Long optionId){

        Product product = optionService.findProductByOptionId(optionId);
        wishListService.deleteWishList(token, product.getId());
    }

    public void sendKakaoMessage(String token, OrderResponse orderResponse){

        String kakaoToken = kakaoTokenService.findKakaoToken(token);
        
        Product product = optionService.findProductByOptionId(orderResponse.getOrder().getOptionId());
        MessageRequest messageRequest = new MessageRequest(kakaoToken, product);
        MessageResponse messageResponse = kakaoApiService.sendMessage(messageRequest);
        if(messageResponse.getCode() != 0){
            throw new CustomException("fail to send message", HttpStatus.BAD_REQUEST, -400);
        }
    }


}
