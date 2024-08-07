package gift.service;

import gift.domain.*;
import gift.dto.request.OrderRequest;
import gift.dto.request.PriceRequest;
import gift.dto.response.OrderPageResponse;
import gift.dto.response.OrderResponse;
import gift.dto.response.PriceResponse;
import gift.exception.*;
import gift.repository.member.MemberSpringDataJpaRepository;
import gift.repository.order.OrderSpringDataJpaRepository;
import gift.repository.product.ProductSpringDataJpaRepository;
import gift.repository.wishlist.WishlistSpringDataJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static gift.exception.ErrorCode.*;

@Service
@Transactional
public class OrderService {

    private final OrderSpringDataJpaRepository orderRepository;
    private final ProductSpringDataJpaRepository productRepository;
    private final WishlistSpringDataJpaRepository wishlistRepository;
    private final MemberSpringDataJpaRepository memberRepository;

    private final OptionService optionService;
    private final KakaoMessageService kakaoMessageService;

    private static final double POINT_EARNING_RATE = 0.1;
    private static final double PRICE_IF_DISCOUNT = 0.9;
    private static final int DISCOUNT_CONDITION_PRICE = 50000;

    @Autowired
    public OrderService(OrderSpringDataJpaRepository orderRepository, ProductSpringDataJpaRepository productRepository, WishlistSpringDataJpaRepository wishlistRepository, MemberSpringDataJpaRepository memberRepository, OptionService optionService, KakaoMessageService kakaoMessageService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.optionService = optionService;
        this.kakaoMessageService = kakaoMessageService;
    }

    public OrderResponse createOrder(TokenAuth tokenAuth, OrderRequest orderRequest) {
        Long optionId = orderRequest.getOptionId();
        Integer quantity = orderRequest.getQuantity();
        Long productId = optionService.getProductIdByOptionId(optionId);
        Member member = tokenAuth.getMember();
        Long memberId = member.getId();
        Integer point = orderRequest.getPoint();
        boolean receipt = orderRequest.isReceipt();
        String phone = orderRequest.getPhone();

        // 포인트
        int memberPoints = member.getPoint();
        if (memberPoints < point) {
            throw new InsufficientPointsException(INSUFFICIENT_POINTS);
        }

        // 현금 영수증 조건 확인
        if (receipt && (phone == null || phone.isBlank())) {
            throw new ReceiptRequiredPhoneException(RECEIPT_REQUIRED_PHONE);
        }

        // 가격
        PriceRequest priceRequest = new PriceRequest(productId, optionId, quantity);
        PriceResponse priceResponse = getOrderPrice(priceRequest);
        int orderPrice = priceResponse.getPrice() - point;
        if (orderPrice < 0) {
            throw new ExcessivePointsException(EXCESSIVE_POINTS);
        }

        memberRepository.subtractPoints(memberId, point);

        optionService.subtractOptionQuantity(productId, optionId, quantity);

        wishlistRepository.deleteByMemberIdAndProductId(memberId, productId);

        Order order = Order.from(orderRequest, orderPrice, true, member);

        orderRepository.save(order);

        if (LoginType.KAKAO.equals(member.getLoginType())){
            kakaoMessageService.sendOrderMessage(tokenAuth.getAccessToken(), order);
        }

        int earnedPoints = (int) (orderPrice * POINT_EARNING_RATE);
        memberRepository.addPoints(memberId, earnedPoints);

        return OrderResponse.fromOrder(order);
    }

    public PriceResponse getOrderPrice(PriceRequest priceRequest){
        Product product = productRepository.findById(priceRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        Long productId = optionService.getProductIdByOptionId(priceRequest.getOptionId());
        if (!product.getId().equals(productId)) {
            throw new OptionNotMatchProductException(OPTION_NOT_MATCH_PRODUCT);
        }

        int orderPrice = product.getPrice() * priceRequest.getQuantity();

        if (orderPrice >= DISCOUNT_CONDITION_PRICE) {
            orderPrice = (int) (orderPrice * PRICE_IF_DISCOUNT);
        }

        return new PriceResponse(orderPrice);
    }

    public OrderPageResponse getOrdersByMemberId(Long memberId, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findByMemberId(memberId, pageable);
        return OrderPageResponse.fromOrderPage(ordersPage);
    }
}
