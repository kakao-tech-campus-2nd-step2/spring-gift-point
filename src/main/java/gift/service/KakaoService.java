package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.domain.KakaoProfile;
import gift.domain.KakaoToken;
import gift.domain.Order;
import gift.domain.Token;
import gift.entity.MemberEntity;
import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import gift.error.NotFoundException;
import gift.repository.OptionRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoService {

    private final static String KAKAO_GET_AUTH_CODE_URL = "https://kauth.kakao.com/oauth/authorize";
    private final static String KAKAO_GET_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_GET_USER_PROFILE = "https://kapi.kakao.com/v2/user/me";
    private final static String KAKAO_SEND_MESSAGE_TO_ME = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private final KakaoProperties kakaoProperties;
    private final MemberService memberService;
    private final RestClient restClient;
    private final OptionRepository optionRepository;

    public KakaoService(KakaoProperties kakaoProperties, MemberService memberService, OptionRepository optionRepository) {
        this.kakaoProperties = kakaoProperties;
        this.memberService = memberService;
        restClient = RestClient.create();
        this.optionRepository = optionRepository;
    }

    public String getCode() {
        String codeUri = UriComponentsBuilder.fromHttpUrl(KAKAO_GET_AUTH_CODE_URL)
            .queryParam("client_id", kakaoProperties.restApiKey())
            .queryParam("redirect_uri", kakaoProperties.redirectUrl())
            .queryParam("response_type", "code")
            .queryParam("scope","talk_message")
            .toUriString();
        return codeUri;
    }

    public KakaoToken getToken(String code){
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.restApiKey());
        body.add("redirect_uri", kakaoProperties.redirectUrl());
        body.add("code", code);

        return restClient.post()
            .uri(KAKAO_GET_TOKEN_URL)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoToken.class)
            .getBody();
    }

    public KakaoProfile getUserProfile(String accessToken) {
        return restClient.get()
            .uri(KAKAO_GET_USER_PROFILE)
            .header("Authorization", "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(KakaoProfile.class)
            .getBody();
    }

    public Token login(String code) {
        KakaoToken kakaoToken = getToken(code);
        KakaoProfile profile = getUserProfile(kakaoToken.access_token());
        return memberService.kakaoLogin(profile.id(), kakaoToken.access_token());
    }

    public void sendOrderMessage(Long memberId, Order order) throws JsonProcessingException {
        MemberEntity memberEntity = memberService.getMemberEntityById(memberId)
            .orElseThrow(() -> new NotFoundException("Not found member"));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", createTemplate(order));

        restClient.post()
            .uri(KAKAO_SEND_MESSAGE_TO_ME)
            .header("Authorization", "Bearer " + memberEntity.getKakaoToken())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve();

        System.out.println("send Message Work!");

    }

    private String createTemplate(Order order) throws JsonProcessingException {

        OptionEntity optionEntity = optionRepository.findById(order.getOptionId())
            .orElseThrow(() -> new NotFoundException("Not found option"));
        ProductEntity productEntity = optionEntity.getProductEntity();
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> links = getLinkMap();
        Map<String, Object> content = getContentMap(productEntity, links);
        Map<String, Object> itemContent = getItemContentMap(productEntity);

        Map<String, Object> message = new HashMap<>();
        message.put("object_type", "feed");
        message.put("content", content);
        message.put("item_content", itemContent);

        return objectMapper.writeValueAsString(message);
    }

    private static Map<String, Object> getItemContentMap(ProductEntity productEntity) {
        Map<String, Object> item = new HashMap<>();
        item.put("item", productEntity.getName());
        item.put("item_op", productEntity.getPrice());

        List<Map<String, Object>> items = List.of(item);

        Map<String, Object> itemContent = new HashMap<>();
        itemContent.put("items", items);
        return itemContent;
    }

    private static Map<String, Object> getLinkMap() {
        Map<String, Object> links = new HashMap<>();
        links.put("web_url", "http://www.daum.net");
        links.put("mobile_web_url", "http://m.daum.net");
        return links;
    }

    private static Map<String, Object> getContentMap(ProductEntity productEntity,
        Map<String, Object> links) {
        Map<String, Object> content = new HashMap<>();
        content.put("title", "주문해 주셔서 감사합니다.");
        content.put("description", productEntity.getCategoryEntity().getDescription());
        content.put("image_url", productEntity.getImageUrl());
        content.put("image_width", 640);
        content.put("image_height", 640);
        content.put("link", links);
        return content;
    }

}
