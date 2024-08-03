package gift.service;

import gift.dto.request.LoginMemberDTO;
import gift.dto.request.OrderPriceRequestDTO;
import gift.dto.request.OrderRequestDTO;
import gift.dto.response.OrderPriceResponseDTO;
import gift.dto.response.OrderResponseDTO;
import gift.dto.response.OrderSuccessResponseDTO;
import gift.dto.response.PagingOrderResponseDTO;
import gift.entity.*;
import gift.exception.memberException.MemberNotFoundException;
import gift.exception.optionException.OptionNotFoundException;
import gift.exception.orderException.DeductPointException;
import gift.exception.productException.ProductNotFoundException;
import gift.repository.*;
import jakarta.transaction.Transactional;
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
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OptionRepository optionRepository,
                        MemberRepository memberRepository,
                        WishRepository wishRepository,
                        RestTemplate restTemplate,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;
        this.restTemplate = restTemplate;
        this.productRepository = productRepository;
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

    /*
    * 추가 : 포인트 차감 로직 추가. excption 제거 후, boolean값 전달해야 함
    * */
    public OrderSuccessResponseDTO addOrder(LoginMemberDTO loginMemberDTO, OrderRequestDTO orderRequestDTO){

        Long optionId = orderRequestDTO.optionId();
        Integer quantity = orderRequestDTO.quantity();
        String message = orderRequestDTO.message();
        Long point = orderRequestDTO.point();
        Long memberId = loginMemberDTO.memberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버를 찾을 수 없습니다"));

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException("옵션 : " + optionId + "번 옵션을 찾을 수 없습니다."));

        Product product = option.getProduct();
        Long productId = product.getId();
        Optional<Wish> wish = wishRepository.findByMemberIdAndProductId(memberId, productId);

        // 주문 entity 만들기 -> 총 얼마나 썼는지 (10% 할인율 적용)
        Long finalPrice = calculateTotalPrice(orderRequestDTO);
        // 주문 만들기
        boolean flag = true;
        // 포인트 감소
        try{
            member.deductPoints(point);
        }catch(DeductPointException e){
            flag = false;
            Order order = new Order(member, option, quantity, message, LocalDateTime.now(), finalPrice, flag);
            orderRepository.save(order);
            return toOrderSuccessDto(order, false);
        }
        Order order = new Order(member, option, quantity, message, LocalDateTime.now(), finalPrice, flag);
        // 주문 저장
        orderRepository.save(order);

        //옵션 수량 빼기
        option.subtract(quantity);

        //wish 제거 -> exception 터지면 안 되는데 ..
        if(wish.isPresent()){
            wishRepository.delete(wish.get());
        }

        //최종 결제 금액 10% 포인트 저장
        Long pointsToAccumulate = (long) (finalPrice * 0.1);
        member.accumulatePoint(pointsToAccumulate);

        // dto 보내기
        OrderSuccessResponseDTO orderSuccessResponseDTO = toOrderSuccessDto(order, flag);
        if(member.getType()==MemberType.KAKAO){
            sendKakaoMessage(member);
        }

        return orderSuccessResponseDTO;
    }

    private Long calculateTotalPrice(OrderRequestDTO orderRequestDTO){
        Long productId= orderRequestDTO.productId();
        Integer quantity = orderRequestDTO.quantity();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        int price = product.getPrice();
        Long totalPrice = (long) price * quantity;

        if (totalPrice > 50_000) {
            totalPrice = (long) (totalPrice * 0.9);
        }

        return totalPrice - orderRequestDTO.point();
    }

    public OrderPriceResponseDTO getOrderPrice(OrderPriceRequestDTO orderPriceRequestDTO){

        Long productId= orderPriceRequestDTO.productId();
        Long optionId = orderPriceRequestDTO.optionId();
        Integer quantity = orderPriceRequestDTO.quantity();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        int price = product.getPrice();

        Long totalPrice = (long) price * quantity;

        if (totalPrice > 50_000) {
            totalPrice = (long) (totalPrice * 0.9);
        }

        return new OrderPriceResponseDTO(totalPrice);
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

    private OrderSuccessResponseDTO toOrderSuccessDto(Order order, boolean success) {
        return new OrderSuccessResponseDTO(
                order.getId(),
                order.getOption()
                        .getId(),
                order.getOrderQuantity(),
                order.getOrderDateTime(),
                order.getMessage(),
                success
        );
    }

}
