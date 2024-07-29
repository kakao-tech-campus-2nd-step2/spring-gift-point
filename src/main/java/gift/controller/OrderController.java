package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.MessageRequestDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.JwtProvider;
import gift.service.OrderService;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final JwtProvider jwtProvider;

    public OrderController(JwtProvider jwtProvider,OrderService orderService) {
        this.jwtProvider = jwtProvider;
        this.orderService = orderService;
    }

    // 로그인 후 상품 주문 , 헤더에 Authorization: Bearer {JWT token} : 기능 요구 사항 (1)+(2)+(3)
    @PostMapping
    public ResponseEntity<OrderResponseDto> order(@RequestHeader("Authorization") String fullToken,@RequestBody OrderRequestDto orderRequestDto){
        String memberEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        return ResponseEntity.status(HttpStatus.OK).body(orderService.order(orderRequestDto,memberEmail));
    }
    // 상품 주문 시 나에게 카카오톡 메시지 전송 : 기능 요구 사항 (4)
    @GetMapping("/message/list")
    public ResponseEntity<String> messageList(@RequestHeader("Authorization") String fullToken){
        String memberEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        List<OrderResponseDto> orders = orderService.findByEmail(memberEmail);
        MessageRequestDto messageRequestDto = new MessageRequestDto();
        messageRequestDto.setText(orders);

        RestTemplate rt = new RestTemplate();
        //Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+orderService.getOauthToken(memberEmail));
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        // body
        MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();
        try {
            params.add("template_object", new ObjectMapper().writeValueAsString(messageRequestDto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("JSON processing error");
        }
        // header + body
        HttpEntity<MultiValueMap<String,Object>> orderListRequest = new HttpEntity<>(params,headers);
        // post로 http 요청을 보내고 응답을 받는다.
        ResponseEntity<String> response = rt.exchange(
            "https://kapi.kakao.com/v2/api/talk/memo/default/send",
            HttpMethod.POST,
            orderListRequest,
            String.class
        );
        return response;
    }
}
