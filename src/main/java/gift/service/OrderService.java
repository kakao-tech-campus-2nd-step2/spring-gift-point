package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.Util.JWTUtil;
import gift.dto.order.*;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.entity.User;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.NotFoundException;
import gift.exception.exception.ServerInternalException;
import gift.exception.exception.UnAuthException;
import gift.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishListRepository wishListRepository;
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    @Value("${kakao.send_message_url}")
    String send_message_url;


    @Transactional
    public OrderResponseDTO order(OrderRequestDTO orderRequestDTO, String token) {
        Option option = findOptionById(orderRequestDTO.optionId());
        Product product = findProductById(orderRequestDTO.productId());
        User user = findUserByIdFromToken(token);

        option.subQuantity(orderRequestDTO.quantity());
        user.subPoint(orderRequestDTO.point());

        String kakaoToken = JWTUtil.getKakaoTokenFromToken(token);

        int price = calcPrice(product.getPrice(), orderRequestDTO.quantity()) - orderRequestDTO.point();
        Order order = new Order(orderRequestDTO, product, option, user, price);

        user.addOrderAndPoint(order);

        if (kakaoToken != null) sendMessage(order, kakaoToken);

        wishListRepository.findByUserAndProduct(user, option.getProduct()).ifPresent(wishList -> wishListRepository.deleteById(wishList.getId()));
        order = orderRepository.save(order);
        return order.toResponseDTO();
    }

    private void sendMessage(Order order, String token) {
        var url = send_message_url;
        var headers = makeSendMessageHeader(token);
        var body = makeSendMessageBody(order);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) throw new UnAuthException("인증되지 않은 요청");
        if (response.getStatusCode() != HttpStatus.OK) throw new BadRequestException("잘못된 요청");
    }

    private HttpHeaders makeSendMessageHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return headers;
    }

    private MultiValueMap<String, String> makeSendMessageBody(Order order) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Map<String, Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", order.getMessage());

        Map<String, String> link = new HashMap<>();
        link.put("web_url", "https://developers.kakao.com");
        link.put("mobile_web_url", "https://developers.kakao.com");
        templateObject.put("link", link);

        String templateObjectJson;
        try {
            templateObjectJson = objectMapper.writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new ServerInternalException("파싱 에러");
        }
        body.add("template_object", templateObjectJson);
        return body;
    }

    public OrderPageDTO getOrders(String token, Pageable pageable) {
        int userId = JWTUtil.getUserIdFromToken(token);
        findUserByIdFromToken(token);
        Page<OrderResponseDTO> orders = orderRepository.findAllWithPage(userId, pageable);
        return new OrderPageDTO(orders);
    }

    private Option findOptionById(int optionId) {
        return optionRepository.findById(optionId)
                .orElseThrow(() -> new NotFoundException("해당 옵션이 없음"));
    }

    private User findUserByIdFromToken(String token) {
        int userId = JWTUtil.getUserIdFromToken(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("유저 없음"));
    }

    public PriceResponseDTO getPrice(PriceRequestDTO priceRequestDTO) {
        Product product = findProductById(priceRequestDTO.productId());
        findOptionById(priceRequestDTO.optionId());
        int price = calcPrice(product.getPrice(), priceRequestDTO.quantity());
        return new PriceResponseDTO(price);
    }

    private Product findProductById(int id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("제품 없음"));
    }

    public int calcPrice(int productPrice, int quantity) {
        int price = productPrice * quantity;
        if (price >= 50000) price = (int) (price * 0.9);
        return price;
    }
}
