package gift.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.option.Option;
import gift.option.OptionRepository;
import gift.option.OptionResponse;
import gift.product.Product;
import gift.product.ProductResponse;
import gift.user.KakaoUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    private final RestClient kakaoRestClient;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    ObjectMapper mapper = new ObjectMapper();

    public OrderService(RestClient kakaoRestClient, OptionRepository optionRepository, OrderRepository orderRepository) {
        this.kakaoRestClient = kakaoRestClient;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
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

        Order order = orderRepository.save(new Order(request, LocalDateTime.now(), user.getId()));

        return new OrderResponse(order);
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

    public List<OrderResponse> getOrderPages(int page, int size, List<String> sort) {

        String sortBy = sort.get(0);
        String sortDirection = sort.get(1);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sortBy)));
        if (Objects.equals(sortDirection, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        }

        return orderRepository.findAll(pageable).map(OrderResponse::new).stream().toList();
    }
}
