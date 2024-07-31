package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.domain.*;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private KakaoService kakaoService;
    private OptionRepository optionRepository;
    private MemberRepository memberRepository;
    private OrderRepository orderRepository;
    private JwtService jwtService;

    public OrderService(KakaoService kakaoService,OptionRepository optionRepository,MemberRepository memberRepository,OrderRepository orderRepository){
        this.kakaoService = kakaoService;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.orderRepository = orderRepository;
    }



    public Order order(String token, OrderRequest orderRequest) throws IllegalAccessException, JsonProcessingException {
        Order order = storeData(token, orderRequest);
        sandOrderMessage(token, orderRequest);
        return order;
    }

    private void sandOrderMessage(String token, OrderRequest orderRequest) throws JsonProcessingException {
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.add("Authorization", token);

        Map<String, Object> link = new HashMap<>();
        link.put("web_url", "https://developers.kakao.com");
        link.put("mobile_web_url", "https://developers.kakao.com");

        Option option = optionRepository.getById(orderRequest.optionId());
        Menu menu = option.getMenu();
        Member member = kakaoService.getUserInformation(token.replace("Bearer ",""));

        String text = "";

        text = text.concat("주문자명 : " + member.getName() + "\n");
        text = text.concat("메뉴명 : " + menu.getName() + "\n");
        text = text.concat("옵션명 : " + option.getName() + "\n");
        text = text.concat("수량 : " + option.getQuantity() + "\n");
        text = text.concat("주문 메세지 : " + orderRequest.message() + "\n");

        var dto = new OrderBodyDto("text",text,link,"바로확인");
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(dto, new TypeReference<Map<String, Object>>() {}); // (3)

        String templateObjectJson = new ObjectMapper().writeValueAsString(map);
        body.add("template_object", templateObjectJson);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }

    private Order storeData(String token, OrderRequest orderRequest) throws IllegalAccessException {
        Option option = optionRepository.getById(orderRequest.optionId());
        option.subtract(orderRequest.quantity());
        optionRepository.save(option);

        Menu menu = option.getMenu();

        Member member = kakaoService.getUserInformation(token.replace("Bearer ",""));
        List<WishList> wishListList = member.getWishList();
        wishListList.removeIf(wishList -> wishList.getMenu().equals(menu));
        member.setWishList(wishListList);
        memberRepository.save(member);

        Date now = new Date();
        Order order = new Order(null,orderRequest.optionId(),orderRequest.quantity(),now,orderRequest.message());
        order = orderRepository.save(order);
        return order;
    }

    public List<Order> findAll(String memberId, Pageable pageable) {

        orderRepository.findByMemberId(memberId,pageable);
    }
}
