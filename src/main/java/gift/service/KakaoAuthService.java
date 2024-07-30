package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.OrderDTO;
import gift.model.*;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.WishRepository;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class KakaoAuthService {
    private static final Logger logger = LoggerFactory.getLogger(KakaoAuthService.class);

    private final RestClient client;

    private final KakaoProperties kakaoProperties;
    private final ProductService productService;
    private final OptionService optionService;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;

    public KakaoAuthService(RestClient.Builder builder, KakaoProperties kakaoProperties, ProductService productService,
                            OptionService optionService, OptionRepository optionRepository, MemberRepository memberRepository, WishRepository wishRepository) {
        this.kakaoProperties = kakaoProperties;
        this.productService = productService;
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(kakaoProperties.getConnectTimeout());
        requestFactory.setReadTimeout(kakaoProperties.getReadTimeout());

        this.client = builder
                .requestFactory(requestFactory)
                .requestInterceptor(new ClientInterceptor())
                .build();
    }


    public String getKakaoToken(String code){
        var url = "https://kauth.kakao.com/oauth/token";

        var body = createBody(code);
        var response =  this.client.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(KakaoAuth.class);
        logger.info("getKakaoToekon" + response);

        //email 카카오 아이디로
        Long memberid = getKakakoMemberId(response.getBody().getAccessToken());
        if(!memberRepository.existsByEmail(memberid.toString())){
            Member member = new Member(memberid.toString(), "password");
            memberRepository.save(member);
        }

        return response.getBody().getAccessToken();
    }

    public void orderProduct(String token, OrderDTO orderDTO) throws JsonProcessingException {
        Long productId = orderDTO.getProductId();
        int quantity = orderDTO.getQuantity();
        //상품 찾음 productID
        Product product = productService.getProductById(productId);
        //프론트에서 옵션 쓰지 않음.
//        //옵션 찾음 productID optionName
//        List<Option> optionList = optionRepository.findAllByProduct_Id(productId);
//        List<String> optionNameList = optionList.stream()
//                .map(Option::getName)
//                .toList();
//        if(!optionNameList.contains(optionName)){
//            throw new IllegalArgumentException("해당 옵션이 없습니다.");
//        }
//        //옵션 수량 만큼 감소(remove) num
//        Optional<Option> Optionaloption = optionRepository.findByNameAndProduct_Id(optionName, productId);
//        Optionaloption.ifPresent(option ->
//                optionService.removeOption(option, num));
        //멤버 ID찾음 token
        Member member = getDBMemberByToken(token);
        //위시리스트에 상품있으면 삭제 멤버iD, productID
        Optional<Wish> OptionalWish = wishRepository.findByMember_IdAndProduct_Id(member.getId(), productId);
        OptionalWish.ifPresent(wish ->
                wishRepository.deleteById(wish.getId()));
        //카카오톡 메세지 보내기
        sendKakaoMessage(token, product.getName(), quantity);
    }

    public Member getDBMemberByToken(String token){
        Long dbMemberId = getKakakoMemberId(token);
        return memberRepository.findByEmail(dbMemberId.toString())
                .orElseThrow(() -> new NoSuchElementException("해당 멤버가 없습니다."));
    }

    public Long getKakakoMemberId(String token){
        var url = "https://kapi.kakao.com/v2/user/me";
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var response = this.client.get()
                .uri(url)
                .headers(httpHeaders -> {
                    httpHeaders.addAll(headers);
                })
                .retrieve()
                .toEntity(KakaoMember.class);
        logger.info("getKakaoMeberId{}", response);
        return response.getBody().getId();
    }

    public void sendKakaoMessage(String token, String productName, int num) throws JsonProcessingException {
        logger.info("sendKakaoMessage");
        var url = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        var body = createTemplateObject(productName, num);

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

    private @NotNull LinkedMultiValueMap<String, String> createBody(String code){
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUrl());
        body.add("code", code);
        return body;
    }

    private @NotNull LinkedMultiValueMap<String, String> createTemplateObject(String productName, int num) throws JsonProcessingException {
        String objectType = "text";
        String text = productName + " 상품이 "
                        + Integer.toString(num) + "개 주문되었습니다.";
        String webUrl = "http://localhost:8080/";
        String buttonTitle = "바로가기";

        LinkObject link = new LinkObject(webUrl);
        TemplateObject templateObject = new TemplateObject(objectType, text, link, buttonTitle);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(templateObject);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", jsonString);

        return body;
    }
}
