package gift.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.option.Option;
import gift.option.OptionRepository;
import gift.option.OptionResponse;
import gift.product.Product;
import gift.user.KakaoUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Date;

@Service
public class OrderService {

    private final RestClient kakaoRestClient;
    private final OptionRepository optionRepository;
    ObjectMapper mapper = new ObjectMapper();

    public OrderService(RestClient kakaoRestClient, OptionRepository optionRepository) {
        this.kakaoRestClient = kakaoRestClient;
        this.optionRepository = optionRepository;
    }

    public OrderResponse sendMessage(KakaoUser user, OrderRequest request, OptionResponse optionResponse) {
        String accessToken = user.getAccessToken();
        System.out.println(accessToken);
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        LinkedMultiValueMap<String, Object> body = createBody(request, optionResponse);

        ResponseEntity<String> response = kakaoRestClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .toEntity(String.class);

        return new OrderResponse(1L, request.optionId, request.quantity, new Date().toString(), request.getMessage());
    }

    private LinkedMultiValueMap<String, Object> createBody(OrderRequest request, OptionResponse optionResponse) {
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        Link link = new Link("http://localhost:8080","http://localhost:8080");
        Content content = new Content("주문이 완료되었습니다.", request.getMessage(), "imgurl", link);
        Option option = optionRepository.findById(optionResponse.id()).orElseThrow();
        Product product = option.getProduct();
        ItemContent itemContent = new ItemContent(product.getImageUrl(), product.getName(), option.getName());
        TemplateObject template_object = new TemplateObject("feed", content, itemContent);

        try {
            String s = mapper.writeValueAsString(template_object);
            body.add("template_object", s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return body;
    }
}
