package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.MemberDTO;
import gift.DTO.OrderDTO;
import gift.entity.OrderEntity;
import gift.exception.ProductNotFoundException;
import gift.mapper.OrderMapper;
import gift.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private WishListService wishListService;

    private final RestClient client = RestClient.builder().build();

    public OrderEntity getOrderEntity(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    public OrderDTO createOrder(OrderDTO orderDTO, MemberDTO memberDTO) {
        var productOptionEntity = productOptionService.getProductOptionEntity(orderDTO.optionId());

        if (orderDTO.quantity() > productOptionEntity.getQuantity()) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        productOptionService.updateProductOptionQuantity(productOptionEntity.getId(), productOptionEntity.getQuantity() - orderDTO.quantity());

        try {
            wishListService.deleteWishListByUserIdAndProductId(productOptionEntity.getProductEntity().getId(), memberDTO.id());
        } catch (ProductNotFoundException e) {
        }

        var orderEntity = orderMapper.toOrderEntity(orderDTO, memberDTO.id(), false);

        orderRepository.save(orderEntity);

        try {
            sendOrderMessage(memberDTO.kakaoAccessToken(), orderDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오톡 메시지 전송에 실패했습니다.");
        }

        return orderMapper.toOrderDTO(orderEntity);
    }

    public void sendOrderMessage(String accessToken, OrderDTO order) throws JsonProcessingException {
        String templateObjectJson = createTemplate(order);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObjectJson);

        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        try {
            client.post()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .toEntity(String.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("카카오톡 메시지 전송에 실패했습니다.");
        }
    }

    private String createTemplate(OrderDTO order) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        // 주문 정보 포함
        String orderDetails = String.format("옵션 id: %s, 수량: %d, 메시지: %s",
                order.optionId(), order.quantity(), order.message());

        Map<String, Object> content = new HashMap<>();
        content.put("title", "주문해 주셔서 감사합니다.");
        content.put("description", "주문 내용: " + order.message());
        content.put("image_url", "http://example.com/image.jpg");
        content.put("image_width", 640);
        content.put("image_height", 640);

        Map<String, Object> message = new HashMap<>();
        message.put("object_type", "text");
        message.put("text", "주문이 완료되었습니다.\n주문 내용: " + orderDetails);
        message.put("link", Map.of(
                "web_url", "www.naver.com",
                "mobile_web_url", "www.google.com"
        ));

        return objectMapper.writeValueAsString(message);
    }
}
