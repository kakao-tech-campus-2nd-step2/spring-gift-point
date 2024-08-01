package gift.service;

import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.domain.member.Member;
import gift.domain.member.SocialAccount;
import gift.dto.OrderDto;
import gift.exception.GiftException;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import gift.service.kakao.KakaoApiService;
import gift.service.kakao.Oauth2TokenService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static gift.exception.ErrorCode.OPTION_NOT_FOUND;

@Transactional
@Service
public class OrderService {

    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final OrderRepository orderRepository;
    private final Oauth2TokenService oauth2TokenService;
    private final KakaoApiService kakaoApiService;

    public OrderService(OptionRepository optionRepository, WishRepository wishRepository, OrderRepository orderRepository, Oauth2TokenService oauth2TokenService, KakaoApiService kakaoApiService) {
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.orderRepository = orderRepository;
        this.oauth2TokenService = oauth2TokenService;
        this.kakaoApiService = kakaoApiService;
    }

    public OrderDto processOrder(OrderDto dto) {
        Member member = dto.getMember();

        Optional<SocialAccount> optionalSocialAccount = Optional.ofNullable(member.getSocialAccount());

        Order order = optionalSocialAccount
                .map(socialAccount -> processWithSocialAccount(socialAccount, dto))
                .orElseGet(() -> processWithoutSocialAccount(dto));

        return OrderDto.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getOrders(Member member, Pageable pageable) {
        Page<Order> orders = orderRepository.findByMember(member, pageable);

        return orders.stream()
                .map(OrderDto::from)
                .toList();
    }

    private Order processWithSocialAccount(SocialAccount socialAccount, OrderDto dto) {
        String accessToken = socialAccount.getAccessToken();
        if (oauth2TokenService.isAccessTokenExpired(accessToken)) {
            oauth2TokenService.refreshAccessToken(socialAccount);
        }
        return processOrderCommon(dto, accessToken);
    }

    private Order processWithoutSocialAccount(OrderDto dto) {
        return processOrderCommon(dto, null);
    }

    private Order processOrderCommon(OrderDto dto, String accessToken) {
        Long optionId = dto.getOptionId();
        Long quantity = dto.getQuantity();
        Member member = dto.getMember();
        String message = dto.getMessage();
        Integer point = dto.getPoint();

        Option option = getOptionById(optionId);
        Product product = option.getProduct();
        int price = product.getPrice();

        option.subtract(quantity);
        member.subtractPoint(point);
        member.earnPoint(price, quantity);
        removeMemberWish(member, product);

        if (accessToken != null) {
            kakaoApiService.sendKakaoMessage(accessToken, message);
        }

        Order order = new Order(option, quantity, message, member);
        return orderRepository.save(order);
    }

    private Option getOptionById(Long optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new GiftException(OPTION_NOT_FOUND));
    }

    private void removeMemberWish(Member member, Product product) {
        wishRepository.findByMemberAndProduct(member, product)
                .ifPresent(wish -> wishRepository.delete(wish));
    }

}
