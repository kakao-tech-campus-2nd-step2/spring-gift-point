package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.betweenKakaoApi.KakaoMsgRequestDTO;
import gift.dto.betweenKakaoApi.KakaoMsgRequestDTO.Content;
import gift.dto.betweenKakaoApi.KakaoMsgRequestDTO.Content.Link;
import gift.dto.betweenKakaoApi.KakaoMsgRequestDTO.ItemContent;
import gift.dto.betweenKakaoApi.KakaoMsgRequestDTO.ItemContent.ItemInfo;
import gift.dto.betweenKakaoApi.KakaoMsgResponseDTO;
import gift.dto.betweenKakaoApi.KakaoUserInfoDTO;
import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenKakaoApi.TokenResponseDTO;
import gift.entity.Option;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.InternalServerExceptions.InternalServerException;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
@PropertySource("classpath:application-kakao-login.properties")
@PropertySource("classpath:application-secret.properties")
public class KakaoTokenService {

    @Value("${kakao-token-url}")
    private String tokenUrl;

    @Value("${kakao-user-info-url}")
    private String userInfoUrl;

    @Value("${kakao-order-URL}")
    private String orderUrl;

    @Value("${kakao-redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${kakao-rest-api-key}")
    private String clientId;

    private final RestClient client = RestClient.builder().build();

    public String getAccessToken(String code){
        try {
            var body = makeBody(clientId, kakaoRedirectUri, code);
            ResponseEntity<TokenResponseDTO> result = client.post()
                    .uri(URI.create(tokenUrl)).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body).retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new BadRequestException("잘못된 요청으로 인한 오류입니다.\n" + response.getBody()
                                .toString().replace("{", "").replace("}", "").trim()
                        );
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new InternalServerException("서버에서 오류가 발생하였습니다.\n" + response.getBody()
                        .toString().replace("{", "").replace("}", "").trim()
                        );
                    }).toEntity(TokenResponseDTO.class);

            return Objects.requireNonNull(result.getBody()).accessToken();
        } catch (BadRequestException | InternalServerException e){
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public MemberDTO getUserInfo(String accessToken){
        try {
            ResponseEntity<KakaoUserInfoDTO> responseUserInfo = client.get()
                    .uri(URI.create(userInfoUrl))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new BadRequestException("잘못된 요청으로 인한 오류입니다.\n" + response.getBody()
                                .toString().replace("{", "").replace("}", "").trim()
                        );
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new InternalServerException("서버에서 오류가 발생하였습니다.\n" + response.getBody()
                                .toString().replace("{", "").replace("}", "").trim()
                        );
                    }).toEntity(KakaoUserInfoDTO.class);

            KakaoUserInfoDTO userInfo = responseUserInfo.getBody();
            KakaoUserInfoDTO.KakaoAccount kakaoAccount = Objects.requireNonNull(userInfo).kakaoAccount();
            KakaoUserInfoDTO.KakaoAccount.Profile profile = kakaoAccount.profile();

            return new MemberDTO(kakaoAccount.email(), "default", "social",  profile.nickname());
        } catch (BadRequestException | InternalServerException e){
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    public void sendMsgToMe(String accessToken, Option option, String message){
        try {
            var body = new LinkedMultiValueMap<String, String>();
            ObjectMapper mapper = new ObjectMapper();
            body.add("template_object", mapper.writeValueAsString(makeOrderMsgDTO(option, message)));

            client.post()
                    .uri(URI.create(orderUrl))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(body).retrieve().onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new BadRequestException("잘못된 요청으로 인한 오류입니다.\n" + response.getBody()
                                .toString().replace("{", "").replace("}", "").trim()
                        );
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new InternalServerException("서버에서 오류가 발생하였습니다.\n" + response.getBody()
                                .toString().replace("{", "").replace("}", "").trim()
                        );
                    }).toEntity(KakaoMsgResponseDTO.class);
        } catch (BadRequestException | InternalServerException e){
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }


    private LinkedMultiValueMap<String, String> makeBody(String clientId, String kakaoRedirectUri, String code){
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_url", kakaoRedirectUri);
        body.add("code", code);
        return body;
    }

    private KakaoMsgRequestDTO makeOrderMsgDTO(Option option, String message){
        DecimalFormat df = new DecimalFormat("###,###");
        Long price = Long.valueOf(option.getProduct().getPrice());

        Link link = new Link("https://github.com/rdme0/spring-gift-order");
        Content content = new Content("https://mud-kage.kakao.com/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg", link);

        ItemInfo itemInfo = new ItemInfo("가격", df.format(price) +"원");
        ItemContent itemContent = new ItemContent(message,
                option.getProduct().getName(), option.getProduct().getImageUrl(),
                option.getName(), new ArrayList<>(), "합계", df.format(price) +"원");
        itemContent.priceList().add(itemInfo);

        return new KakaoMsgRequestDTO("feed", content, itemContent);
    }

}
