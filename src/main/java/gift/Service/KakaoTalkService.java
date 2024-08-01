package gift.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.Entity.Option;
import gift.Entity.Product;
import gift.Model.Link;
import gift.Model.OrderRequestDto;
import gift.Model.TextObject;
import gift.Repository.OptionJpaRepository;
import gift.Repository.ProductJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class KakaoTalkService {

    private final RestTemplate restTemplate;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private OptionJpaRepository optionJpaRepository;

    public KakaoTalkService() {
        this.restTemplate = new RestTemplate();
    }

    public void sendMessageToMe(String token, List<OrderRequestDto> orderRequestDtoList) throws Exception {
        // 카카오톡 메시지 전송
        StringBuilder message = new StringBuilder();
        message.append("주문 내역\n");
        for (OrderRequestDto orderRequestDto : orderRequestDtoList) {
            Product product = productJpaRepository.findById(orderRequestDto.getProductId()).orElseThrow();
            Option option = optionJpaRepository.findById(orderRequestDto.getOptionId()).orElseThrow();
            message.append("상품 이름 : ")
                    .append(product.getName())
                    .append("\n")
                    .append("옵션 이름 : ")
                    .append(option.getName())
                    .append("\n")
                    .append("수량 : ")
                    .append(orderRequestDto.getQuantity())
                    .append("개\n\n");
        }

        long totalPrice = orderRequestDtoList.stream()
                .mapToLong(orderRequestDto -> {
                    Product product = productJpaRepository.findById(orderRequestDto.getProductId()).orElseThrow();
                    int price = product.getPrice();
                    int quantity = orderRequestDto.getQuantity();

                    return (long) price * quantity;
                })
                .sum();

        message.append("총 주문 금액 : ").append(totalPrice).append("원\n");
        message.append("정상적으로 주문이 완료되었습니다.");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        TextObject textObject = new TextObject(
                "text",
                message.toString(),
                new Link("https://developers.kakao.com", "https://developers.kakao.com")
        );

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonTextObject = objectMapper.writeValueAsString(textObject);
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("template_object", jsonTextObject);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/api/talk/memo/default/send",
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            System.out.println("Response: " + response.getBody());
        } catch (HttpStatusCodeException e) {
            System.err.println("HTTP Status Code: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
        }

    }

}
