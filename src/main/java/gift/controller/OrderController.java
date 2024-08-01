package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.Member;
import gift.dto.CommonResponse;
import gift.dto.MessageRequestDto;
import gift.dto.OrderRequestDto;
import gift.dto.OrderResponseDto;
import gift.service.JwtProvider;
import gift.service.MemberService;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final JwtProvider jwtProvider;
    private final RestTemplate restTemplate;
    private final MemberService memberService;

    public OrderController(JwtProvider jwtProvider,OrderService orderService,RestTemplate restTemplate, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.orderService = orderService;
        this.restTemplate = restTemplate;
        this.memberService = memberService;
    }

    // 로그인 후 상품 주문 , 헤더에 Authorization: Bearer {JWT token} : 기능 요구 사항 (1)+(2)+(3)
    @Operation(summary = "새로운 주문을 생성합니다")
    @PostMapping
    public ResponseEntity<CommonResponse> order(@RequestHeader("Authorization") String fullToken,@RequestBody OrderRequestDto orderRequestDto){
        String memberEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        OrderResponseDto orderResponseDto = orderService.order(orderRequestDto,memberEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse(orderResponseDto, "주문 생성 성공", true));
    }
    // 상품 주문 시 나에게 카카오톡 메시지 전송 : 기능 요구 사항 (4)
    @GetMapping("/message/list")
    public ResponseEntity<String> messageList(@RequestHeader("Authorization") String fullToken){
        String memberEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        List<OrderResponseDto> orders = orderService.findByEmail(memberEmail);
        MessageRequestDto messageRequestDto = new MessageRequestDto();
        messageRequestDto.setText(orders);

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
        ResponseEntity<String> response = restTemplate.exchange(
            "https://kapi.kakao.com/v2/api/talk/memo/default/send",
            HttpMethod.POST,
            orderListRequest,
            String.class
        );
        return response;
    }

    @Operation(summary = "주문 목록을 페이지 단위로 조회합니다")
    @GetMapping
    public ResponseEntity<CommonResponse> getPagedOrders(@RequestHeader("Authorization") String fullToken,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,desc") String sort,
        Pageable pageable){

        String userEmail = jwtProvider.getMemberEmail(fullToken.substring(7));
        Member member = memberService.findByEmail(userEmail);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new CommonResponse(orderService.getPagedOrders(member,pageable),"페이징 된 주문 목록 조회 성공",true));
    }
}
