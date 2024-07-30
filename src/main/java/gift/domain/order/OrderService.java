package gift.domain.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import gift.domain.wish.WishService;
import gift.domain.member.Member;
import gift.domain.member.dto.LoginInfo;
import gift.domain.wish.JpaWishRepository;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.OptionService;
import gift.domain.member.JpaMemberRepository;
import gift.global.exception.BusinessException;
import gift.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final JpaWishRepository cartItemRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JpaMemberRepository memberRepository;
    private final WishService wishService;

    @Autowired
    public OrderService(
        JpaOptionRepository jpaOptionRepository,
        OptionService optionService,
        JpaWishRepository jpaWishRepository,
        RestTemplate restTemplate,
        ObjectMapper objectMapper,
        JpaMemberRepository memberRepository,
        WishService wishService
    ) {
        optionRepository = jpaOptionRepository;
        this.optionService = optionService;
        cartItemRepository = jpaWishRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.memberRepository = memberRepository;
        this.wishService = wishService;
    }

    /**
     * (나에게) 상품 선물하기
     */
    public void order(OrderRequestDTO orderRequestDTO, LoginInfo loginInfo) {
        orderProduct(orderRequestDTO, loginInfo);
        sendMessage(orderRequestDTO, loginInfo);
    }
    @Transactional
    public void orderProduct(OrderRequestDTO orderRequestDTO, LoginInfo loginInfo) {
        // 해당 상품의 옵션의 수량을 차감
        optionService.decreaseOptionQuantity(orderRequestDTO.optionId(),
            orderRequestDTO.quantity());

        // 해당 상품이 (나의) 위시리스트에 있는 경우 위시 리스트에서 삭제
        wishService.deleteWishIfExists(loginInfo.getId(), orderRequestDTO.optionId());
    }
    private void sendMessage(OrderRequestDTO orderRequestDTO, LoginInfo loginInfo) {
        // 메세지 작성
        MultiValueMap<String, String> body = createTemplateObject(
            orderRequestDTO);

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
        OrderRequestDTO orderRequestDTO) {
        TemplateObject templateObject = new TemplateObject(orderRequestDTO.message());
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

}
