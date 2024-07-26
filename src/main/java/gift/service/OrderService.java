package gift.service;

import gift.exception.ErrorCode;
import gift.exception.RepositoryException;
import gift.model.Option;
import gift.model.Order;
import gift.model.OrderDTO;
import gift.model.Product;
import gift.model.WishList;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishListRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishListRepository wishListRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishListRepository = wishListRepository;
        this.restTemplate = restTemplate;
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Option option = optionRepository.findById(orderDTO.optionId())
            .orElseThrow(() -> new RepositoryException(
                ErrorCode.OPTION_NOT_FOUND, orderDTO.optionId()));

        Product product = option.getProduct();
        if (wishListRepository.findWishListByProductId(product.getId()).isPresent()) {
            WishList wishlist = wishListRepository.findWishListByProductId(product.getId())
                .orElseThrow(() -> new RuntimeException("해당 위시 리스트를 찾을 수 없습니다."));
            wishListRepository.deleteById(wishlist.getId());
        }
        option.subtract(orderDTO.quantity());
        Order order = new Order(orderDTO.optionId(), orderDTO.quantity(), orderDTO.message());
        order.updateOrderDate(LocalDateTime.now()
            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return convertToDTO(orderRepository.save(order));
    }

    public void sendOrderMessage(OrderDTO orderDTO, String accessToken) {
        String url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("{\"object_type\":\"text\",\"text\":\"")
            .append(orderDTO.message())
            .append("\",\"link\":{\"web_url\":\"\"}}");

        body.add("template_object", sb.toString());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

    }

    private OrderDTO convertToDTO(Order order) {
        return new OrderDTO(order.getId(), order.getOptionId(), order.getQuantity(),
            order.getOrderDate(), order.getMessage());
    }
}
