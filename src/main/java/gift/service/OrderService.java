package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.OrderDTO;
import gift.model.entity.Member;
import gift.model.entity.Option;
import gift.model.entity.Wish;
import gift.model.kakao.LinkObject;
import gift.model.kakao.TemplateObject;
import gift.repository.WishRepository;
import gift.service.intercptor.ClientInterceptor;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);

    private final RestClient client;

    private final OptionService optionService;
    private final KakaoAuthService kakaoAuthService;
    private final WishRepository wishRepository;

    public OrderService(RestClient.Builder builder, KakaoProperties kakaoProperties, OptionService optionService, KakaoAuthService kakaoAuthService, WishRepository wishRepository) {
        this.optionService = optionService;
        this.kakaoAuthService = kakaoAuthService;
        this.wishRepository = wishRepository;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(kakaoProperties.getConnectTimeout());
        requestFactory.setReadTimeout(kakaoProperties.getReadTimeout());

        this.client = builder
                .requestFactory(requestFactory)
                .requestInterceptor(new ClientInterceptor())
                .build();
    }

    public void orderProduct(String token, OrderDTO orderDTO) throws JsonProcessingException {
        int quantity = orderDTO.getQuantity();
        Option option = optionService.getOption(orderDTO.getOptionId());

        //옵션 수량 만큼 감소(remove) num
        optionService.removeOption(option, quantity);
        //멤버 ID찾음 token
        Member member = kakaoAuthService.getDBMemberByToken(token);
        //위시리스트에 상품있으면 삭제 멤버iD, productID
        Optional<Wish> OptionalWish = wishRepository.findByMember_IdAndProduct_Id(member.getId(), option.getProductID());
        OptionalWish.ifPresent(wish ->
                wishRepository.deleteById(wish.getId()));
        //카카오톡 메세지 보내기
        sendKakaoMessage(token, orderDTO.getMessage());
    }

    public void sendKakaoMessage(String token, String message) throws JsonProcessingException {
        logger.info("sendKakaoMessage");
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var body = createTemplateObject(message);

        var response = this.client.post()
                .uri(url)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headers);
                })
                .body(body)
                .retrieve()
                .toEntity(String.class);

        logger.info("sendKakaoMessget result" + response);
    }
    private @NotNull LinkedMultiValueMap<String, String> createTemplateObject(String message) throws JsonProcessingException {
        String objectType = "text";
        String webUrl = "http://localhost:8080/";
        String buttonTitle = "바로가기";

        LinkObject link = new LinkObject(webUrl);
        TemplateObject templateObject = new TemplateObject(objectType, message, link, buttonTitle);

        String jsonString = new ObjectMapper().writeValueAsString(templateObject);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", jsonString);

        return body;
    }
}
