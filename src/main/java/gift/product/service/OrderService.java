package gift.product.service;

import static gift.product.exception.GlobalExceptionHandler.INVALID_HTTP_REQUEST;
import static gift.product.exception.GlobalExceptionHandler.NOT_RECEIVE_RESPONSE;
import static gift.product.intercepter.AuthInterceptor.AUTHORIZATION_HEADER;
import static gift.product.intercepter.AuthInterceptor.BEARER_PREFIX;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.OrderRequestDTO;
import gift.product.dto.OrderResponseDTO;
import gift.product.exception.RequestException;
import gift.product.exception.ResponseException;
import gift.product.model.Member;
import gift.product.model.Option;
import gift.product.model.Order;
import gift.product.repository.OptionRepository;
import gift.product.repository.OrderRepository;
import gift.product.util.JwtUtil;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final OrderRepository orderRepository;
    private final RestClient client = RestClient.builder()
        .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
            throw new RequestException(INVALID_HTTP_REQUEST);
        })
        .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
            throw new ResponseException(NOT_RECEIVE_RESPONSE);
        })
        .build();
    private final OptionService optionService;

    @Autowired
    public OrderService(
        OptionService optionService,
        ObjectMapper objectMapper,
        JwtUtil jwtUtil,
        OrderRepository orderRepository) {
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
        this.orderRepository = orderRepository;
        this.optionService = optionService;
    }

    public OrderResponseDTO orderProduct(
        String authorization,
        OrderRequestDTO orderRequestDTO) {
        System.out.println("[OrderService] orderProduct()");
        Member member = jwtUtil.parsingToken(authorization);
        Option option = optionService.subtractQuantity(orderRequestDTO);
        Order order = orderRequestDTO.convertToDomain(option, member);
        member.subtractPoint(order);
        orderRepository.save(order);
        if(member.getSnsMember() != null)
            sendToMe(order);
        return new OrderResponseDTO(order);
    }

    public void sendToMe(Order order) {
        System.out.println("[OrderService] sendToMe()");
        final var body = createBody(
            "[ 주문 내역 ]"
                + "\n옵션 명: " + order.getOption().getName()
                + "\n수량: " + order.getQuantity()
                + "\n메세지: " + order.getMessage());
        String kakaoAccessToken = order.getMember().getSnsMember().getAccessToken();
        postRequest(kakaoAccessToken, body);
    }

    private @NotNull LinkedMultiValueMap<String, Object> createBody(String message) {
        var body = new LinkedMultiValueMap<String, Object>();
        try {
            body.add("template_object", objectMapper.writeValueAsString(
                Map.of(
                    "object_type", "text",
                    "text", message,
                    "link", Map.of(
                        "web_url", "",
                        "mobile_web_url", ""
                    )
                )
            ));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return body;
    }

    private void postRequest(
        String kakaoAccessToken,
        LinkedMultiValueMap<String, Object> body) {
        client.post()
            .uri(URI.create("https://kapi.kakao.com/v2/api/talk/memo/default/send"))
            .header(AUTHORIZATION_HEADER, BEARER_PREFIX + kakaoAccessToken)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(String.class);
    }

    public Page<OrderResponseDTO> getAllOrders(String authorization, Pageable pageable) {
        Member member = jwtUtil.parsingToken(authorization);
        Page<Order> myOrders =  orderRepository.findAllByMember(pageable, member);
        return convertToResponseDTOList(myOrders, pageable);
    }

    private Page<OrderResponseDTO> convertToResponseDTOList(Page<Order> myOrders, Pageable pageable) {
        List<OrderResponseDTO> orders = myOrders.stream()
            .map(this::convertToResponseDTO)
            .collect(Collectors.toList());
        return new PageImpl<>(
            orders,
            pageable,
            myOrders.getTotalElements()
        );
    }

    private OrderResponseDTO convertToResponseDTO(Order order) {
        return new OrderResponseDTO(order);
    }
}
