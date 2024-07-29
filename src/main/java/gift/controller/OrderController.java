package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.MemberDto;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.service.MemberService;
import gift.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

@Controller
public class OrderController {

    private final OrderService orderService;
    private MemberService memberService;
    @Value("${secret.key}")
    private String secretKey;

    public OrderController(MemberService memberService, OrderService orderService) {
        this.memberService = memberService;
        this.orderService = orderService;
    }

    @PostMapping("/api/order")
    public ResponseEntity<OrderResponse> getOrderRequest(@RequestHeader(name = "Authorization") String authHead,
        @RequestBody OrderRequest orderRequest) throws JsonProcessingException {

        var accessToken = authHead.replace("Bearer ", "");

        // 멤버 정보 필요
        var memberDto = new MemberDto("ks4264@naver.com", "ks4264@naver.com");

        // orderResponse
        var orderResponse = new OrderResponse(orderRequest.getOptionId(),
            orderRequest.getQuantity(), orderRequest.getMessage());

        var headers = getHttpHeadersForSelfMessage(accessToken);
        // 주문 처리 -> 위시 리스트 삭제, 옵션 수량 감소
        var bodyTemplate = orderService.processOrder(memberDto, orderRequest);

        // 메세지 전송
        var responseEntity = getHttpSendSelfMessage(bodyTemplate, headers);

        String responseBody = responseEntity.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        // 결과 확인
        var resultCode = jsonNode.get("result_code").asText();
        if (resultCode.equals("0")) {
            return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(orderResponse, HttpStatus.BAD_REQUEST);
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
        return rt.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );
    }

    public ResponseEntity<String> getHttpSendSelfMessage(MultiValueMap<String, String> body,
        HttpHeaders headers) {
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(
            body, headers);
        RestTemplate rt = new RestTemplateBuilder().build();
        return rt.exchange(
            "https://kapi.kakao.com/v2/api/talk/memo/default/send",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );
    }

}
