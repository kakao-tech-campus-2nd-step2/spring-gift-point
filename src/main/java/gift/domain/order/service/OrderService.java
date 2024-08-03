package gift.domain.order.service;

import gift.domain.member.entity.Member;
import gift.domain.option.entity.Option;
import gift.domain.option.service.OptionService;
import gift.domain.order.dto.OrderRequest;
import gift.domain.order.dto.OrderResponse;
import gift.domain.order.entity.Orders;
import gift.domain.order.exception.OrderException;
import gift.domain.order.repository.OrderRepository;
import gift.domain.wishlist.entity.Wish;
import gift.domain.wishlist.repository.WishRepository;
import gift.kakaoApi.service.KakaoApiService;
import gift.oauth.service.OAuthService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final WishRepository wishRepository;
    private final OrderRepository orderRepository;
    private final KakaoApiService kakaoApiService;
    private final OptionService optionService;
    private final OAuthService oauthService;

    private static final int DISCOUNT_THRESHOLD = 50_000;
    private static final double DISCOUNT_RATE = 0.1;

    public OrderService(WishRepository wishRepository, OrderRepository orderRepository,
        KakaoApiService kakaoApiService, OptionService optionService, OAuthService oauthService) {
        this.wishRepository = wishRepository;
        this.orderRepository = orderRepository;
        this.kakaoApiService = kakaoApiService;
        this.optionService = optionService;
        this.oauthService = oauthService;
    }

    public Page<OrderResponse> getAllOrders(Pageable pageable, Member member) {
        return orderRepository.findAllByMember(pageable, member).map(this::entityToDto);
    }

    @Transactional
    public OrderResponse createOrder(Member member, OrderRequest request) {
        Orders newOrder = dtoToEntity(member, request);
        if(hasSufficientPoints(newOrder, member)) {
            orderProcess(newOrder);
        }
        return entityToDto(orderRepository.save(newOrder));
    }

    private boolean hasSufficientPoints(Orders order, Member member){
        int totalPrice = calculateTotalPrice(order);

        if (member.getPoint() < totalPrice) {
            throw new OrderException("포인트가 부족합니다.");
        }

        member.updatePoint(member.getPoint() - totalPrice);

        return true;
    }

    private int calculateTotalPrice(Orders order) {
        int totalPrice = order.getOption().getProduct().getPrice() * order.getQuantity();

        if (totalPrice >= DISCOUNT_THRESHOLD) {
            totalPrice = (int) Math.round(totalPrice * (1 - DISCOUNT_RATE));
        }

        return totalPrice;
    }

    private void orderProcess(Orders order) {

        optionService.subtractQuantity(order.getOption().getId(), order.getQuantity());
        removeIfInWishlist(order.getMember(), order.getOption());

        String kakaoAccessToken = order.getMember().getKakaoAccessToken();

        if (kakaoAccessToken == null) {
            return;
        }

        if (kakaoApiService.sendKakaoMessage(kakaoAccessToken, order)) {
            oauthService.registerOrLoginKakoMember(kakaoAccessToken);
        }
    }
    private void removeIfInWishlist(Member member, Option option) {
        Optional<Wish> wish = wishRepository.findByProductAndMember(option.getProduct(), member);
        wish.ifPresent(wishRepository::delete);
    }

    private Orders dtoToEntity(Member member, OrderRequest request) {
        Option savedOption = optionService.getOption(request.getOptionId());
        return new Orders(savedOption, member, request.getQuantity(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")),
            request.getMessage());
    }

    private OrderResponse entityToDto(Orders order) {
        return new OrderResponse(order.getId(), order.getOption().getId(), order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage());
    }
}
