package gift.product.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.product.dto.auth.KakaoMessage;
import gift.product.dto.auth.Link;
import gift.product.dto.auth.LoginMemberIdDto;
import gift.product.dto.order.OrderDto;
import gift.product.exception.LoginFailedException;
import gift.product.model.KakaoToken;
import gift.product.model.Option;
import gift.product.model.Order;
import gift.product.repository.AuthRepository;
import gift.product.repository.KakaoTokenRepository;
import gift.product.repository.OptionRepository;
import gift.product.repository.OrderRepository;
import gift.product.repository.WishRepository;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Transactional(readOnly = true)
public class OrderService {

    private static final String LINK_URL = "http://localhost:8080";
    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final OptionRepository optionRepository;
    private final AuthRepository authRepository;
    private final KakaoTokenRepository kakaoTokenRepository;
    private final RestClient restClient = RestClient.builder().build();

    public OrderService(OrderRepository orderRepository,
        WishRepository wishRepository,
        OptionRepository optionRepository,
        AuthRepository authRepository,
        KakaoTokenRepository kakaoTokenRepository) {
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
        this.optionRepository = optionRepository;
        this.authRepository = authRepository;
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public List<Order> getOrderAll(LoginMemberIdDto loginMemberIdDto) {
        return orderRepository.findAllByMemberId(loginMemberIdDto.id());
    }

    public Order getOrder(Long id, LoginMemberIdDto loginMemberIdDto) {
        return orderRepository.findByIdAndMemberId(id, loginMemberIdDto.id())
            .orElseThrow(() -> new NoSuchElementException("해당 ID의 주문 내역이 존재하지 않습니다."));
    }

    @Transactional
    public Order doOrder(OrderDto orderDto,
        LoginMemberIdDto loginMemberIdDto,
        String externalApiUrl) {
        Order order = processOrder(orderDto, loginMemberIdDto);
        LinkedMultiValueMap<String, String> body = getRequestBody(
            orderDto);

        ResponseEntity<String> response = restClient.post()
            .uri(URI.create(externalApiUrl))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + getKakaoToken(loginMemberIdDto).getAccessToken())
            .body(body)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, ((req, res) -> {
                throw new LoginFailedException("카카오톡 메시지 API 관련 에러가 발생하였습니다. 다시 시도해주세요.");
            }))
            .toEntity(String.class);

        checkApiResultCode(response);
        return order;
    }

    @Transactional
    public void deleteOrder(Long orderId, LoginMemberIdDto loginMemberIdDto) {
        validateExistenceOrder(orderId);
        orderRepository.deleteByIdAndMemberId(orderId, loginMemberIdDto.id());
    }

    private Option getValidatedOption(Long optionId) {
        return optionRepository.findById(optionId)
            .orElseThrow(() -> new NoSuchElementException("해당 ID의 옵션이 존재하지 않습니다."));
    }

    private void validateExistenceMember(Long memberId) {
        if (!authRepository.existsById(memberId)) {
            throw new NoSuchElementException("해당 ID의 회원이 존재하지 않습니다.");
        }
    }

    private void validateExistenceOrder(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NoSuchElementException("해당 ID의 주문 내역이 존재하지 않습니다.");
        }
    }

    private LinkedMultiValueMap<String, String> getRequestBody(OrderDto orderDto) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Link link = new Link(LINK_URL, LINK_URL);
        KakaoMessage kakaoMessage = new KakaoMessage("text", orderDto.message(), link);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            body.add("template_object", objectMapper.writeValueAsString(kakaoMessage));
        } catch (Exception e) {
            throw new LoginFailedException("소셜 로그인 진행 중 예기치 못한 오류가 발생하였습니다. 다시 시도해 주세요.");
        }
        return body;
    }

    private void checkApiResultCode(ResponseEntity<String> response) {
        int resultCode = -1;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            resultCode = rootNode.path("result_code").asInt();
        } catch (Exception e) {
            throw new LoginFailedException("소셜 로그인 진행 중 예기치 못한 오류가 발생하였습니다. 다시 시도해 주세요.");
        }

        if (resultCode != 0) {
            throw new LoginFailedException("카카오톡 메시지 API 관련 에러가 발생하였습니다. 다시 시도해주세요.");
        }
    }

    private Order processOrder(OrderDto orderDto, LoginMemberIdDto loginMemberIdDto) {
        Option option = getValidatedOption(orderDto.optionId());
        Long productId = option.getProduct().getId();
        validateExistenceMember(loginMemberIdDto.id());
        option.subtract(orderDto.quantity());
        optionRepository.save(option);

        if (wishRepository.existsByProductIdAndMemberId(productId, loginMemberIdDto.id())) {
            wishRepository.deleteByProductIdAndMemberId(productId, loginMemberIdDto.id());
        }

        return orderRepository.save(new Order(orderDto.optionId(), loginMemberIdDto.id(),
            orderDto.quantity(), orderDto.message()));
    }

    private KakaoToken getKakaoToken(LoginMemberIdDto loginMemberIdDto) {
        return kakaoTokenRepository.findByMemberId(loginMemberIdDto.id())
            .orElseThrow(() -> new NoSuchElementException("카카오 계정 로그인을 수행한 후 다시 시도해주세요."));
    }
}


