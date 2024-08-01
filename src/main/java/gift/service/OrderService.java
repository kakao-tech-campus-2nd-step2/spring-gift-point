package gift.service;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Member;
import gift.model.Option;
import gift.model.Order;
import gift.model.OrderRequest;
import gift.model.OrderResponse;
import gift.model.Product;
import gift.model.WishList;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishListRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishListRepository wishListRepository;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishListRepository wishListRepository, MemberRepository memberRepository,
        RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishListRepository = wishListRepository;
        this.memberRepository = memberRepository;
        this.restTemplate = restTemplate;
    }

    public OrderResponse createOrder(OrderRequest request) {
        Option option = optionRepository.findById(request.optionId())
            .orElseThrow(() -> new RepositoryException(
                ErrorCode.OPTION_NOT_FOUND, request.optionId()));

        Product product = option.getProduct();

        if (wishListRepository.findWishListByProductId(product.getId()).isPresent()) {
            WishList wishlist = wishListRepository.findWishListByProductId(product.getId())
                .orElseThrow(() -> new RuntimeException("해당 위시 리스트를 찾을 수 없습니다."));
            wishListRepository.deleteById(wishlist.getId());
        }

        option.subtract(request.quantity());
        Order order = orderRepository.save(
            new Order(request.optionId(), request.quantity(), request.message()));
        long totalPrice = product.getPrice() * request.quantity();
        long discountedPrice = request.point(); // 나중에 만약 포인트 이외에 할인 쿠폰의 기능이 있다면...
        long accumulatedPoint = (totalPrice - discountedPrice) / 10;
        return new OrderResponse(order.getOptionId(), totalPrice, discountedPrice,
            accumulatedPoint);
    }

    public void sendOrderMessage(OrderRequest request, long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new RepositoryException(ErrorCode.MEMBER_NOT_FOUND, memberId));
        String accessToken = member.getPassword();
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"object_type\":\"text\",\"text\":\"")
            .append(request.message())
            .append("\",\"link\":{\"web_url\":\"\"}}");

        body.add("template_object", sb.toString());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }
}
