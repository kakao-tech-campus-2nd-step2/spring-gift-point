package gift.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.annotation.LoginMember;
import gift.member.model.Member;
import gift.member.service.MemberService;
import gift.dto.HttpResult;
import gift.order.dto.OrderRequest;
import gift.order.dto.OrderResponse;
import gift.order.model.Order;
import gift.order.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private MemberService memberService;
    @Value("${secret.key}")
    private String secretKey;

    public OrderController(MemberService memberService, OrderService orderService) {
        this.memberService = memberService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> addOrder(
        @RequestHeader(name = "Authorization") String authHead,
        @LoginMember Member member,
        @RequestBody OrderRequest orderRequest) throws JsonProcessingException {

        var accessToken = authHead.replace("Bearer ", "");
        var headers = getHttpHeadersForSelfMessage(accessToken);
        // 주문 처리 -> 위시 리스트 삭제, 옵션 수량 감소
        var order = orderService.addOrder(member, orderRequest);

        if (member.getKakaoId() == null) {
            return ResponseEntity.ok(
                new OrderResponse(HttpResult.OK, "주문 추가 성공", HttpStatus.OK, order));
        }

        MultiValueMap<String, String> bodyTemplate = orderService.makeOrderMessage();

        return getOrderResponseResponseEntity(bodyTemplate, headers, order);
    }


    private ResponseEntity<OrderResponse> getOrderResponseResponseEntity(
        MultiValueMap<String, String> bodyTemplate, HttpHeaders headers,
        Order order) throws JsonProcessingException {

        // 메세지 전송
        var responseEntity = getHttpSendSelfMessage(bodyTemplate, headers);

        String responseBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // 결과 확인
        var resultCode = jsonNode.get("result_code").asText();
        if (resultCode.equals("0")) {
            return ResponseEntity.ok(
                new OrderResponse(HttpResult.OK, "카카오 선물하기 메세지 전송 성공", HttpStatus.OK, order));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            new OrderResponse(HttpResult.ERROR, "카카오 회원이 아닙니다", HttpStatus.UNAUTHORIZED, null));
    }

    @GetMapping
    public ResponseEntity<OrderResponse> getOrderListPage(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "sort", defaultValue = "orderDateTime,DESC") String sort,
        @LoginMember Member member
    ) {
        String[] sortParams = sort.split(",");
        String property = sortParams[0];
        Direction direction =
            sortParams.length > 1 ? Direction.valueOf(sortParams[1]) : Direction.ASC;
        Page<Order> orderListPage = orderService.getOrderListPage(member, page, size, property,
            direction);
        return ResponseEntity.ok(
            new OrderResponse(HttpResult.OK, "주문 리스트 조회 성공", HttpStatus.OK, orderListPage));
    }

    public HttpHeaders getHttpHeadersForSelfMessage(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    public ResponseEntity<String> getHttpResponse(HttpHeaders headers) {
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplateBuilder().build();
        return rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST,
            kakaoUserInfoRequest, String.class);
    }

    public ResponseEntity<String> getHttpSendSelfMessage(MultiValueMap<String, String> body,
        HttpHeaders headers) {
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(body,
            headers);
        RestTemplate rt = new RestTemplateBuilder().build();
        return rt.exchange("https://kapi.kakao.com/v2/api/talk/memo/default/send", HttpMethod.POST,
            kakaoUserInfoRequest, String.class);
    }

}
