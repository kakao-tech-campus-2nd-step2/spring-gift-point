package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoMessage;
import gift.dto.KakaoTokenResponseDto;
import gift.dto.Link;
import gift.exception.ProductNotFoundException;
import gift.model.Member;
import gift.model.Option;
import gift.model.Order;
import gift.repository.OrderRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private final OptionService optionService;

    @Autowired
    private final WishlistService wishlistService;

    private final RestClient restClient;

    public OrderService(OrderRepository orderRepository,OptionService optionService, WishlistService wishlistService) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        restClient = RestClient.create();
    }

    public Order save(Order order) {
        if (!optionService.existsOptionById(order.getOptionId())){
            throw new ProductNotFoundException("Option not found");
        }
        Option selectedOption = optionService.getOptionById(order.optionId).get();
        optionService.subtractOption(selectedOption, order.quantity);
        wishlistService.deleteById(selectedOption.getProduct().getId());
        LocalDateTime dateTime = LocalDateTime.now();
        order.setOrderDateTime(dateTime);
        return orderRepository.save(order);
    }

    public void sendMessage(Order order, Member member) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoMessage kakaoMessage = new KakaoMessage("text",order.getMessage(),new Link("link"));
        String kakaoMessageJson = null;
        kakaoMessageJson = objectMapper.writeValueAsString(kakaoMessage);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.set("template_object", kakaoMessageJson);

        restClient.post()
            .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header(AUTHORIZATION, BEARER + member.getKakaoToken())
            .body(map)
            .retrieve()
            .toEntity(String.class);


    }

}
