package gift.user.service;

import gift.exception.BadRequestException;
import gift.exception.ResourceNotFoundException;
import gift.order.entity.Order;
import gift.product.entity.Option;
import gift.product.entity.Product;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import gift.user.entity.User;
import gift.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Service
public class KakaoApiService {

  private final WebClient webClient;
  private final UserRepository userRepository;
  private final OptionRepository optionRepository;
  private final ProductRepository productRepository;


  @Value("${kakao.client-id}")
  private String clientId;

  @Value("${kakao.redirect-uri}")
  private String redirectUri;

  public KakaoApiService(Builder webClientBuilder, UserRepository userRepository,
      OptionRepository optionRepository, ProductRepository productRepository) {
    this.webClient = webClientBuilder.baseUrl("https://kauth.kakao.com").build();
    this.userRepository = userRepository;
    this.optionRepository = optionRepository;
    this.productRepository = productRepository;

  }

  public Mono<String> getKakaoToken(String authorizationCode) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("grant_type", "authorization_code");
    body.add("client_id", clientId);
    body.add("redirect_uri", redirectUri);
    body.add("code", authorizationCode);

    return webClient.post()
        .uri("/oauth/token")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .body(BodyInserters.fromFormData(body))
        .retrieve()
        .bodyToMono(String.class);
  }

  public Mono<Map<String, Object>> getKakaoUserInfo(String accessToken) {
    MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    body.add("property_keys", "[\"kakao_account.email\"]");

    return webClient.post()
        .uri("https://kapi.kakao.com/v2/user/me")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .body(BodyInserters.fromFormData(body))
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
  }

  public Mono<Void> saveOrUpdateUser(Map<String, Object> userInfo) {
    return Mono.fromRunnable(() -> {
      Long kakaoId = (Long) userInfo.get("id");
      String email = "kakao_" + kakaoId + "@kakao.com";

      Optional<User> userOptional = userRepository.findByEmail(email);
      User user;
      if (userOptional.isPresent()) {
        user = userOptional.get();
      } else {
        user = new User();
        user.setEmail(email);
      }

      userRepository.save(user);
    });
  }

  public Mono<Void> sendOrderMessage(Order order, String userEmail) {
    return userRepository.findByEmail(userEmail)
        .map(user -> {
          String accessToken = user.getKakaoAccessToken();
          Option option = optionRepository.findById(order.getOptionId())
              .orElseThrow(() -> new ResourceNotFoundException("옵션을 찾을 수 없습니다."));
          Product product = productRepository.findById(option.getProduct().getId())
              .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다."));
          String templateObject = buildTemplateObject(order, option, product);

          MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
          body.add("template_object", templateObject);

          return webClient.post()
              .uri("https://kapi.kakao.com/v2/api/talk/memo/default/send")
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
              .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
              .body(BodyInserters.fromFormData(body))
              .retrieve()
              .bodyToMono(String.class)
              .doOnNext(response -> System.out.println("메시지가 성공적으로 전송되었습니다: " + response))
              .doOnError(error -> {
                throw new BadRequestException("메시지를 전송하는 중에 오류가 발생했습니다.");
              })
              .then();
        }).orElse(Mono.error(new ResourceNotFoundException("사용자를 찾을 수 없습니다.")));
  }

  private String buildTemplateObject(Order order, Option option, Product product) {
    return "{\n" +
        "  \"object_type\": \"feed\",\n" +
        "  \"content\": {\n" +
        "    \"title\": \"주문이 완료되었습니다.\",\n" +
        "    \"description\": \"" + order.getMessage() + "\",\n" +
        "    \"image_url\": \"https://mud-kage.kakao.com/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg\",\n" +
        "    \"link\": {\n" +
        "      \"web_url\": \"http://www.daum.net\",\n" +
        "      \"mobile_web_url\": \"http://m.daum.net\"\n" +
        "    }\n" +
        "  },\n" +
        "  \"item_content\": {\n" +
        "    \"title_image_url\": \"" + product.getImageUrl() + "\",\n" +
        "    \"title_image_text\": \"" + product.getName() + "\",\n" +
        "    \"title_image_category\": \"" + option.getName() + "\"\n" +
        "  }\n" +
        "}";
  }

}
