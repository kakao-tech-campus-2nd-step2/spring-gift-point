package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.constants.Messages;
import gift.domain.Member;
import gift.domain.Order;
import gift.domain.Wish;
import gift.dto.MessageRequestDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponse;
import gift.dto.OrderResponseDto;
import gift.exception.OrderNotFoundException;
import gift.repository.OrderRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final MemberService memberService;
    private final OptionService optionService;
    private final WishService wishService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OrderService(MemberService memberService, OptionService optionService,
        WishService wishService, ProductService productService, OrderRepository orderRepository
        , RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.optionService = optionService;
        this.wishService = wishService;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;

    }

    public OrderResponseDto order(OrderRequestDto orderRequestDto, String memberEmail) {
        Member member = memberService.findByEmail(memberEmail);
        optionService.subtractQuantity(orderRequestDto.getOptionId(),
            orderRequestDto.getQuantity());
        Long productId = optionService.findById(orderRequestDto.getOptionId()).getProduct().getId();
        Optional<Wish> wishOptional = wishService.findByEmailAndProductId(memberEmail, productId);
        if (wishOptional.isPresent()) {
            Wish wish = wishOptional.get();
            wishService.deleteById(wish.getId());
        }

        Order order = new Order(orderRequestDto.getOptionId(), member.getId(),
            orderRequestDto.getQuantity(), orderRequestDto.getMessage());
        orderRepository.save(order);
        return convertToOrderResponseDto(order);
    }

    public List<OrderResponseDto> findByEmail(String memberEmail) {
        Member member = memberService.findByEmail(memberEmail);
        List<Order> orders = orderRepository.findByMemberId(member.getId())
            .orElseThrow(() -> new OrderNotFoundException(Messages.NOT_FOUND_ORDER_MESSAGE));
        return orders.stream()
            .map(this::convertToOrderResponseDto)
            .collect(Collectors.toList());
    }

    public String getOauthToken(String memberEmail) {
        Member member = memberService.findByEmail(memberEmail);
        return member.getAccessToken();
    }

    private OrderResponseDto convertToOrderResponseDto(Order order) {
        return new OrderResponseDto(
            order.getId(),
            order.getOptionId(),
            order.getQuantity(),
            order.getOrderDateTime(),
            order.getMessage()
        );
    }

    public ResponseEntity<String> sendKakaoMessage(String memberEmail) {
        List<OrderResponseDto> orders = findByEmail(memberEmail);
        MessageRequestDto messageRequestDto = new MessageRequestDto();
        messageRequestDto.setText(orders);

        // Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getOauthToken(memberEmail));
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // Body
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        try {
            params.add("template_object", objectMapper.writeValueAsString(messageRequestDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("JSON processing error");
        }

        // Header + Body
        HttpEntity<MultiValueMap<String, Object>> orderListRequest = new HttpEntity<>(params,
            headers);

        // Post 요청
        return restTemplate.exchange(
            "https://kapi.kakao.com/v2/api/talk/memo/default/send",
            HttpMethod.POST,
            orderListRequest,
            String.class
        );
    }

    @Transactional
    public Page<OrderResponse> getPagedOrders(Member member, Pageable pageable) {
        Page<Order> pagedOrders = orderRepository.findByMemberId(member.getId(), pageable);
        return pagedOrders.map(OrderResponse::from);
    }
}
