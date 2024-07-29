package gift.Service;

import gift.Model.Entity.OptionEntity;
import gift.Model.Entity.OrderEntity;
import gift.Model.request.OrderRequest;
import gift.Model.response.OrderResponse;
import gift.Repository.OptionRepository;
import gift.Repository.OrderRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(OptionRepository optionRepository, OrderRepository orderRepository, RestTemplate restTemplate){
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public OrderResponse order(OrderRequest orderRequest){
        Optional<OptionEntity> optionEntityOptional = optionRepository.findById(orderRequest.optionId());
        if(optionEntityOptional.isEmpty()){
            throw new IllegalArgumentException("해당 옵션이 없습니다.");
        }

        OptionEntity optionEntity = optionEntityOptional.get();
        optionEntity.subtract(orderRequest.quantity());
        optionRepository.save(optionEntity);

        OrderEntity orderEntity = new OrderEntity(optionEntity, orderRequest.quantity(), LocalDateTime.now(), orderRequest.message());
        orderRepository.save(orderEntity);

        return new OrderResponse(orderEntity.getId(), orderRequest.optionId(), orderEntity.getQuantity(), orderEntity.getOrderDateTime(), orderEntity.getMessage());
    }

    public void sendKakaoTalkMessage(String accessToken, OrderResponse orderResponse){
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"object_type\":\"text\",\"text\":\"")
                .append(orderResponse)
                .append("\",\"link\":{\"web_url\":\"\"}}");

        body.add("template_object",sb.toString());

        RequestEntity request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
        ResponseEntity response = new RestTemplate().exchange(request, Map.class);
    }
}
