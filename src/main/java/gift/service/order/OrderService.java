package gift.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.oAuthToken.OAuthTokenRepository;
import gift.domain.order.Order;
import gift.domain.order.OrderRepository;
import gift.mapper.OrderMapper;
import gift.service.category.CategoryService;
import gift.service.option.OptionService;
import gift.service.wish.WishService;
import gift.web.dto.category.CategoryResponseDto;
import gift.web.dto.MemberDto;
import gift.web.dto.order.OrderRequestDto;
import gift.web.dto.order.OrderResponseDto;
import gift.web.dto.product.ProductResponseDto;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final OrderMapper orderMapper;
    private final RestClient restClient;
    private final WishService wishService;
    private final CategoryService categoryService;
    private final OAuthTokenRepository oAuthTokenRepository;

    @Value("${kakao.send-message-template-url}")
    private String kakaoSendMessageTemplateUrl;

    public OrderService(
        OrderRepository orderRepository,
        OAuthTokenRepository oAuthTokenRepository,
        OptionService optionService,
        OrderMapper orderMapper,
        RestClient restClient,
        WishService wishService,
        CategoryService categoryService) {
        this.orderRepository = orderRepository;
        this.oAuthTokenRepository = oAuthTokenRepository;
        this.optionService = optionService;
        this.orderMapper = orderMapper;
        this.restClient = restClient;
        this.wishService = wishService;
        this.categoryService = categoryService;
    }

    @Transactional
    public OrderResponseDto createOrder(MemberDto memberDto, OrderRequestDto orderRequestDto) {

        ProductResponseDto productResponseDto = optionService.getProduct(orderRequestDto.optionId());
        CategoryResponseDto categoryResponseDto = categoryService.getCategory(productResponseDto.categoryId());

        if (wishService.existsByMemberEmailAndProductId(memberDto.email(), productResponseDto.id())) {
            wishService.deleteWish(memberDto.email(), productResponseDto.id());
        }

        optionService.subtractOptionQuantity(orderRequestDto.optionId(), productResponseDto.id(), orderRequestDto.quantity());

        Order order = orderRepository.save(orderMapper.toEntity(
            orderRequestDto, optionService.getOptionEntityByOptionId(orderRequestDto.optionId())));


        oAuthTokenRepository.findByMemberEmail(memberDto.email())
            .ifPresent(token -> {
                sendOrderKakaoMessage(productResponseDto, categoryResponseDto, order, token.getToken());
            });

        return orderMapper.toDto(order);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendOrderKakaoMessage(ProductResponseDto productResponseDto, CategoryResponseDto categoryResponseDto, Order order, String token) {

        Map<String, String> templateArgs = new HashMap<>();
        templateArgs.put("product_name", productResponseDto.name());
        templateArgs.put("category_name", categoryResponseDto.name());
        templateArgs.put("price", productResponseDto.price().toString());
        templateArgs.put("quantity", order.getQuantity().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String templateArgsJson = "";

        try {
            templateArgsJson = objectMapper.writeValueAsString(templateArgs);
        } catch (JsonProcessingException e) {
            new RuntimeException("메시지 생성 에러 발생");
        }

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_id", "110540");
        body.add("template_args", templateArgsJson);

        restClient.post()
            .uri(URI.create(kakaoSendMessageTemplateUrl))
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
    }
}
