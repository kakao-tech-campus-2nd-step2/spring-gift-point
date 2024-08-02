package gift.service;

import gift.dto.request.LoginMemberDTO;
import gift.dto.request.OrderRequestDTO;
import gift.dto.response.OrderResponseDTO;
import gift.dto.response.PagingOrderResponseDTO;
import gift.entity.*;
import gift.exception.memberException.MemberNotFoundException;
import gift.exception.optionException.OptionNotFoundException;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OptionRepository optionRepository,
                        MemberRepository memberRepository,
                        WishRepository wishRepository,
                        RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;
        this.restTemplate = restTemplate;
    }

    public List<OrderResponseDTO> getAllOrders(LoginMemberDTO loginMemberDTO){
        Long memberId = loginMemberDTO.memberId();
        List<Order> orders = orderRepository.findByMemberId(memberId);
        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public PagingOrderResponseDTO getOrders(LoginMemberDTO loginMemberDTO, Pageable pageable){
        Long memberId = loginMemberDTO.memberId();

        Page<Order> orderPage = orderRepository.findBymemberId(memberId, pageable);

        List<OrderResponseDTO> orderResponseDTOs = orderPage.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new PagingOrderResponseDTO(
                orderResponseDTOs,
                orderPage.getNumber(),
                (int) orderPage.getTotalElements(),
                orderPage.getSize(),
                orderPage.isLast()
        );
    }


    //주문하기
    /*
    * 1. member - wish에 있다면 삭제해야 함.
    * 2. option 수량 차감해야 함.
    * 3. member - order 목록 추가해야 함.
    *
    *
    * 1. 일단 상품 존재해?
    * 2. 존재하구나 -> 장바구니에 있나?
    * 3. 장바구니에 있네 -> 지우기. 없네? -> 그냥
    * */


    public OrderResponseDTO addOrder(LoginMemberDTO loginMemberDTO, OrderRequestDTO orderRequestDTO){

        Long optionId = orderRequestDTO.optionId();
        Integer quantity = orderRequestDTO.quantity();
        String message = orderRequestDTO.message();

        Long memberId = loginMemberDTO.memberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버를 찾을 수 없습니다"));

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException("옵션 : " + optionId + "번 옵션을 찾을 수 없습니다."));

        Product product = option.getProduct();
        Long productId = product.getId();
        Optional<Wish> wish = wishRepository.findByMemberIdAndProductId(memberId, productId);

        if(wish.isPresent()){
            wishRepository.delete(wish.get());
        }

        option.subtract(quantity);

        Order order = new Order(member, option, quantity, message, LocalDateTime.now());
        orderRepository.save(order);

        OrderResponseDTO orderResponseDTO= toDto(order);
        if(member.getType()==MemberType.KAKAO){
            sendKakaoMessage(member);
        }

        return orderResponseDTO;
    }


    private void sendKakaoMessage(Member member ){
        String kakaoAccessToken = member.getKakaoToken();
        String jsonBody = "{"
                + "\"object_type\": \"text\","
                + "\"text\": \"상품 주문 완료\","
                + "\"link\": {"
                + "  \"web_url\": \"https://developers.kakao.com\","
                + "  \"mobile_web_url\": \"https://developers.kakao.com\""
                + "},"
                + "\"button_title\": \"바로 확인\""
                + "}";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", jsonBody);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Authorization", "Bearer " + kakaoAccessToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("메시지 전송 성공");
        } else {
            System.out.println("메시지 전송 실패: " + response.getStatusCode());
        }

    }

    private OrderResponseDTO toDto(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getOption()
                        .getId(),
                order.getOrderQuantity(),
                order.getOrderDateTime(),
                order.getMessage()
        );
    }



}
