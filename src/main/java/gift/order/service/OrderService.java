package gift.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.exception.KakaoTokenException;
import gift.member.domain.Member;
import gift.member.repository.MemberRepository;
import gift.member.service.MemberService;
import gift.option.domain.Option;
import gift.option.service.OptionService;
import gift.order.domain.OrderTotalPrice;
import gift.order.dto.OrderListResponseDto;
import gift.order.dto.OrderResponseDto;
import gift.order.dto.OrderServiceDto;
import gift.order.domain.Order;
import gift.order.dto.TemplateObject;
import gift.order.exception.OrderNotFoundException;
import gift.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final OptionService optionService;
    private final MemberRepository memberRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public static final String BEARER_TYPE = "Bearer ";
    private static final String MESSAGE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private static final String WEB_URL = "https://developers.kakao.com";
    private static final String MOBILE_WEB_URL = "https://developers.kakao.com";

    public OrderService(OrderRepository orderRepository, MemberService memberService, OptionService optionService, MemberRepository memberRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.optionService = optionService;
        this.memberRepository = memberRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public OrderListResponseDto getAllOrders() {
        return OrderListResponseDto.orderListToOptionListResponseDto(orderRepository.findAll());
    }

    public OrderResponseDto getOrderById(Long id) {
        return OrderResponseDto.orderToOrderResponseDto(orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new));
    }

    @Transactional
    public Order createOrder(OrderServiceDto orderServiceDto) {
        Member member = memberService.getMemberById(orderServiceDto.memberId());
        Option option = optionService.getOptionById(orderServiceDto.optionId());
        option.subtract(orderServiceDto.quantity().getOrderCountValue());
        sendMessage(orderServiceDto.memberId(), orderServiceDto.message().getOrderMessageValue());
        Long totalPrice = option.getProduct().getPrice().getProductPriceValue() * orderServiceDto.quantity().getOrderCountValue();
        return orderRepository.save(orderServiceDto.toOrder(member, option, new OrderTotalPrice(totalPrice)));
    }

    public Order updateOrder(OrderServiceDto orderServiceDto) {
        validateOrderExists(orderServiceDto.id());
        Member member = memberService.getMemberById(orderServiceDto.memberId());
        Option option = optionService.getOptionById(orderServiceDto.optionId());
        return orderRepository.save(orderServiceDto.toOrder(member, option));
    }

    public void deleteOrder(Long id) {
        validateOrderExists(id);
        orderRepository.deleteById(id);
    }

    private void validateOrderExists(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException();
        }
    }

    private void sendMessage(Long memberId, String message) {
        String accessToken = memberRepository.findAccessTokenById(memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, BEARER_TYPE + accessToken);

        Map<String, String> link = new HashMap<>();
        link.put("web_url", WEB_URL);
        link.put("mobile_web_url", MOBILE_WEB_URL);

        TemplateObject templateObject = new TemplateObject("text", message, link, "바로 확인");

        String templateObjectJson;
        try {
            templateObjectJson = objectMapper.writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new KakaoTokenException();
        }

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObjectJson);

        HttpEntity<LinkedMultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                URI.create(MESSAGE_URL),
                HttpMethod.POST,
                request,
                String.class
        );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new KakaoTokenException();
        }
    }
}
