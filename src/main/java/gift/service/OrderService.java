package gift.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.dto.WishlistRequest;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.User;
import gift.entity.Wishlist;
import gift.exception.InvalidOptionException;
import gift.exception.InvalidPointException;
import gift.exception.InvalidTokenFormatException;
import gift.exception.InvalidUserException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishlistRepository;

@Service
public class OrderService {

	private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final WishlistRepository wishlistRepository;
    private final KakaoMessageService kakaoMessageService;
    private final WishlistService wishlistService;
    private final UserService userService;
    private final RetryTemplate retryTemplate;
    private final PointService pointService;

    public OrderService(OptionRepository optionRepository, OrderRepository orderRepository,
                        WishlistRepository wishlistRepository, KakaoMessageService kakaoMessageService,
                        WishlistService wishlistService, UserService userService,
                        RetryTemplate retryTemplate, PointService pointService) {
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.wishlistRepository = wishlistRepository;
        this.kakaoMessageService = kakaoMessageService;
        this.wishlistService = wishlistService;
        this.retryTemplate = retryTemplate;
        this.userService = userService;
        this.pointService = pointService;
    }
    
    public Page<OrderResponse> getOrders(String token, Pageable pageable){
    	User user = userService.getUserFromToken(token);
    	Page<Order> OrderPage = orderRepository.findByUserId(user.getId(), pageable);
    	return OrderPage.map(Order::toDto);
    }
    
    @Transactional
    public OrderResponse createOrder(String token, OrderRequest request, BindingResult bindingResult) {
	    return retryTemplate.execute(context -> {
    		validateBindingResult(bindingResult);
	        Option option = updateOptionQuantity(request);
	        User user = userService.getUserFromToken(token);
	        
	        int totalPrice = calculateTotalPrice(request, option);
            int pointsToUse = processPayment(user, totalPrice);;
	        
            Order order = saveOrder(user, request, option, totalPrice - pointsToUse); 
            orderTasks(token, user, option, request, bindingResult);
	        return order.toDto();
	    });
    }
    
    private int calculateTotalPrice(OrderRequest request, Option option) {
        int totalPrice = option.getProduct().getPrice() * request.getQuantity();
        return applyDiscount(totalPrice);
    }
    
    private int applyDiscount(int totalPrice) {
        if (totalPrice >= 50000) {
            totalPrice -= (int) (totalPrice * 0.1);
        }
        return totalPrice;
    }
    
    private int processPayment(User user, int totalPrice) {
        if (!pointService.hasSufficientPoints(user.getId(), totalPrice)) {
            throw new InvalidPointException("Not enough points.");
        }
        return pointService.deductPoints(user.getId(), totalPrice);
    }
    
    private void orderTasks(String token, User user, Option option, OrderRequest request,
    		BindingResult bindingResult){
    	removeWishlistOnOrder(user, option.getProduct().getId(), token, bindingResult);
        sendKakaoMessage(token, request.getMessage());
    }
    
    private Option updateOptionQuantity(OrderRequest request) {
    	Option option = findOption(request.getOptionId());
    	option.decreaseQuantity(request.getQuantity());
    	optionRepository.save(option);
    	return option;
    }
    
    private Order saveOrder(User user, OrderRequest request, Option option, int finalPrice) {
        Order order = request.toEntity(user, option);
        order.setOrderDateTime(LocalDateTime.now());
        order.setFinalPrice(finalPrice);
        return orderRepository.save(order);
    }


    private Option findOption(Long optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new InvalidOptionException("Option not found."));
    }

    private void sendKakaoMessage(String token, String message) {
        String accessToken = validateExtractToken(token);
        kakaoMessageService.sendMessage(accessToken, message);
    }

    private String validateExtractToken(String token) {
        if (!token.startsWith("Bearer ")) {
            throw new InvalidTokenFormatException("Invalid token format.");
        }
        return token.substring(7);
    }

    private void removeWishlistOnOrder(User user, Long productId, String token, BindingResult bindingResult) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(user.getId(), productId);
        if (wishlist != null) {
        	WishlistRequest request = new WishlistRequest(productId);
            wishlistService.removeWishlist(token, request, bindingResult);
        }
    }

    private void validateBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            throw new InvalidUserException(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }
}
