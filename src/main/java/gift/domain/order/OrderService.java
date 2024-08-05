package gift.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import gift.domain.option.Option;
import gift.domain.order.dto.request.OrderRequest;
import gift.domain.order.dto.response.OrderPageResponse;
import gift.domain.order.dto.response.OrderResponse;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.domain.wish.WishService;
import gift.domain.member.Member;
import gift.domain.member.dto.LoginInfo;
import gift.domain.wish.JpaWishRepository;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.OptionService;
import gift.domain.member.JpaMemberRepository;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import gift.global.exception.option.OptionNotFoundException;
import gift.global.exception.point.PointNotEnoughException;
import gift.global.exception.product.ProductNotFoundException;
import gift.global.exception.user.MemberNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final String SEND_ME_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private final JpaOptionRepository optionRepository;
    private final OptionService optionService;
    private final JpaWishRepository wishRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JpaMemberRepository memberRepository;
    private final WishService wishService;
    private final JpaProductRepository productRepository;
    private final JpaOrderRepository orderRepository;

    @Autowired
    public OrderService(
        JpaOptionRepository jpaOptionRepository,
        OptionService optionService,
        JpaWishRepository jpaWishRepository,
        RestTemplate restTemplate,
        ObjectMapper objectMapper,
        JpaMemberRepository memberRepository,
        WishService wishService,
        JpaProductRepository productRepository,
        JpaOrderRepository orderRepository
    ) {
        optionRepository = jpaOptionRepository;
        this.optionService = optionService;
        wishRepository = jpaWishRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
        this.wishService = wishService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * (나에게) 상품 선물하기
     */
    public void order(OrderRequest orderRequest, LoginInfo loginInfo) {
        orderProduct(orderRequest, loginInfo);
        sendMessage(orderRequest, loginInfo);
    }
    @Transactional
    public void orderProduct(OrderRequest orderRequest, LoginInfo loginInfo) {
        // 해당 상품의 옵션의 수량을 차감
        optionService.decreaseOptionQuantity(orderRequest.optionId(),
            orderRequest.quantity());

        // 해당 상품이 (나의) 위시리스트에 있는 경우 위시 리스트에서 삭제
        wishService.deleteWishIfExists(loginInfo.getId(), orderRequest.optionId());

        // 보유 포인트에서 사용 포인트 차감
        usePoint(orderRequest.point(), loginInfo.getId());

        // 포인트 적립(사용 현금의 5%)
        Member member = memberRepository.findById(loginInfo.getId()).get();
        Product product = productRepository.findById(orderRequest.productId())
            .orElseThrow(() -> new ProductNotFoundException(orderRequest.productId()));
        int usedCash = product.getPrice() * orderRequest.quantity().intValue() - orderRequest.point(); // 포인트 제외 사용금액
        member.chargePoint((int) (usedCash * 0.05));

        // 주문 정보 저장
        Option option = optionRepository.findById(orderRequest.optionId()).get();
        Order order = orderRequest.toOrder(member, product, option);
        orderRepository.save(order);
    }

    private void usePoint(int usePoint, Long userId) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new MemberNotFoundException(userId));
        if(member.getPoint() < usePoint){ // 보유 포인트가 사용 포인트보다 적은 경우
            throw new PointNotEnoughException(usePoint, member.getPoint());
        }
        member.usePoint(usePoint);
    }

    private void sendMessage(OrderRequest orderRequest, LoginInfo loginInfo) {
        // 메세지 작성
        MultiValueMap<String, String> body = createTemplateObject(
            orderRequest);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        Member member = memberRepository.findById(loginInfo.getId()).get();
        headers.setBearerAuth(member.getAccessToken()); // 엑세스 토큰

        // (나에게) 메시지 전송
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Object> response = restTemplate.exchange(SEND_ME_URL, HttpMethod.POST,
                requestEntity, Object.class);
            System.out.println("Response: " + response.getBody());
        } catch (HttpClientErrorException e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "카카오톡 엑세스 토큰이 유효하지 않습니다.");
        }
    }

    // 나에게 메시지 보내기 DOCS 에 나와 있는 데이터 형식
    private MultiValueMap<String, String> createTemplateObject(
        OrderRequest orderRequest) {
        TemplateObject templateObject = new TemplateObject(orderRequest.message());
        String textTemplateJson;
        try {
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
            textTemplateJson = objectMapper.writeValueAsString(templateObject);
        } catch (JsonProcessingException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "json 형식이 올바르지 않습니다.");
        }
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", textTemplateJson);
        return body;
    }

    public OrderPageResponse getOrders(Long memberId, PageRequest pageRequest) {
        Page<Order> orderPage = orderRepository.findAllByMemberId(memberId, pageRequest);
        boolean hasNext = orderPage.hasNext();
        List<OrderResponse> ordersResponse = orderPage.stream().map(order -> order.toOrderResponse())
            .toList();
        return new OrderPageResponse(hasNext, ordersResponse);
    }
}
