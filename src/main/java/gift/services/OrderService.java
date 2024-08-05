package gift.services;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.KaKaoUserDto;
import gift.dto.MemberDto;
import gift.dto.OrderDto;
import gift.dto.OrderPageDto;
import gift.dto.PageInfoDto;
import gift.dto.RequestOrderDto;
import gift.repositories.MemberRepository;
import gift.repositories.OptionRepository;
import gift.repositories.OrderRepository;
import gift.repositories.ProductRepository;
import gift.repositories.WishRepository;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;


    @Value("${kakao.api.userInfo.uri}")
    private String KAKAO_API_USERINFO_URI;
    @Value("${kakao.api.send.message.uri}")
    private String KAKAO_API_SEND_MESSAGE_URI;

    public OrderService(OrderRepository orderRepository, OptionRepository optionRepository,
        WishRepository wishRepository, ProductRepository productRepository,
        MemberRepository memberRepository) {
        this.orderRepository = orderRepository;
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public OrderDto addOrder(RequestOrderDto requestOrderDto, MemberDto memberDto) {
        Member member = memberRepository.findByEmail(memberDto.getEmail());

        Option option = optionRepository.findById(requestOrderDto.getOptionId()).orElseThrow(
            () -> new IllegalArgumentException(
                "Option not found with id " + requestOrderDto.getOptionId()));

        member.decrementPoint(requestOrderDto.getPoint());
        option.decrementAmount(requestOrderDto.getQuantity());

        deleteWishWithOptionId(memberDto.getId(), option);

        Order order = orderRepository.save(
            new Order(member, option, requestOrderDto.getQuantity(),
                requestOrderDto.getMessage()));

        member.addPoint(option.getProduct().getPrice(), requestOrderDto.getQuantity());

//        sendKaKaoMessage(requestOrderDto, token);

        return order.toOrderDto();
    }

    public OrderPageDto getOrderById(Long memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Order> orders = orderRepository.findAllByMemberId(memberId, pageable);
        List<OrderDto> orderDtoList = orders.stream().map(Order::toOrderDto).toList();

        PageInfoDto pageInfo = new PageInfoDto(page, orders.getTotalElements(),
            orders.getTotalPages());

        return new OrderPageDto(pageInfo, orderDtoList);
    }

    public void sendKaKaoMessage(RequestOrderDto requestOrderDto, String token) {
        String messageText = String.format("주문이 완료되었습니다! 주문한 옵션: %s, 수량: %d",
            requestOrderDto.getOptionId(),
            requestOrderDto.getQuantity());

        String jsonMessage = String.format(
            "{\"template_object\": {\"object_type\": \"text\", \"text\": \"%s\", \"link\": {\"web_url\": \"https://www.naver.com\"}}}",
            messageText);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonMessage, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            KAKAO_API_SEND_MESSAGE_URI,
            HttpMethod.POST,
            httpEntity,
            String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Message has been sent successfully.");
        } else {
            System.out.println("Failed to send message." + response.getBody());
        }
    }

    private void deleteWishWithOptionId(Long memberId, Option option) {
        Product product = option.getProduct();
        Wish wishToDelete = wishRepository.findAllByMemberId(memberId).stream()
            .filter(wish -> wish.getProduct().getId().equals(product.getId())) // Product ID 비교
            .findFirst()
            .orElse(null);

        if (wishToDelete != null) {
            wishRepository.delete(wishToDelete);
        }
    }

    private KaKaoUserDto getUserInfoWithToken(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            KAKAO_API_USERINFO_URI,
            HttpMethod.POST,
            httpEntity,
            String.class
        );

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
            long id = (long) jsonObj.get("id");
            return new KaKaoUserDto(id);
        } catch (ParseException e) {
            System.out.println("Failed to parse user info response: " + e.getMessage());
            return null;
        }
    }
}

