package gift.order;

import com.google.gson.Gson;
import gift.option.Option;
import gift.option.OptionService;
import gift.product.Product;
import gift.wishes.Wish;
import gift.wishes.WishService;
import java.net.URI;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionService optionService;
    private final WishService wishService;
    private final RestClient client;


    public OrderService(OrderRepository orderRepository,
        OptionService optionService, WishService wishService, RestClient client) {
        this.orderRepository = orderRepository;
        this.optionService = optionService;
        this.wishService = wishService;
        this.client = client;
    }

    public OrderInfo saveOrder(OrderRequest orderRequest, Long memberId) {
        Long optionId = orderRequest.getOptionId();
        Option option = optionService.getOption(optionId);
        option.subtract(orderRequest.getQuantity());
        OrderInfo orderInfo = orderRequest.toEntity();
        orderInfo.setMemberId(memberId);

        return orderRepository.save(orderRequest.toEntity());
    }

    public LinkedMultiValueMap<String, String> setMessage(OrderRequest orderRequest) {

        LinkedMultiValueMap<String, String> template = new LinkedMultiValueMap<String, String>();
        MessageTemplate messageTemplate = new MessageTemplate(
            "text",
            orderRequest.getMessage(),
            new Link("")
        );

        Gson gson = new Gson();

        String templateObjectJson = gson.toJson(messageTemplate);
        template.add("template_object", templateObjectJson);

        return template;
    }

    public void sendMessage(OrderRequest orderRequest, String accessToken) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        var template = setMessage(orderRequest);

        var response = client.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + accessToken)
            .body(template) // request body
            .retrieve()
            .toEntity(String.class);
    }

    public void deleteOrderedProduct(OrderRequest orderRequest, Long memberId) {
        Long optionId = orderRequest.getOptionId();
        Product product = optionService.getProduct(optionId);
        Wish wish = wishService.getWish(product.getId(), memberId);
        wishService.deleteWish(wish.getId(), memberId);
    }

    public List<OrderInfo> getOrderList(Long memberId){
        return orderRepository.findByMemberId(memberId);
    }


}
