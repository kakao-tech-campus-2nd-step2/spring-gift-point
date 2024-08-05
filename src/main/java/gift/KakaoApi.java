package gift;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.DTO.KakaoJwtToken;
import gift.DTO.KakaoJwtTokenDto;
import gift.DTO.OptionDto;
import gift.DTO.OrderDto;
import gift.Exception.UnauthorizedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoApi {

  RestClient restClient = RestClient.builder().build();
  private RestTemplate restTemplate;

  public KakaoJwtTokenDto kakaoLoginApiPost(String url, String API_KEY, String autuhorizationKey) {
    var body = new LinkedMultiValueMap<String, String>();

    body.add("grant_type", "authorization_code");
    body.add("client_id", API_KEY);
    body.add("redirect_url", "http://localhost:8080");
    body.add("code", autuhorizationKey);

    String response = restClient.post()
      .uri(url)
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .body(body)
      .retrieve()
      .body(String.class);

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      JsonNode jsonNode = objectMapper.readTree(response);
      String accessToken = jsonNode.get("access_token").asText();
      String refreshToken = jsonNode.get("refresh_token").asText();
      KakaoJwtTokenDto kakaoJwtTokenDto = new KakaoJwtTokenDto(1L, accessToken, refreshToken);

      return kakaoJwtTokenDto;

    } catch (Exception e) {
      throw new UnauthorizedException(
        "KOE320 : 동일한 인가 코드를 두 번 이상 사용하거나, 이미 만료된 인가 코드를 사용한 경우, 혹은 인가 코드를 찾을 수 없는 경우입니다.");
    }
  }

  public void kakaoSendMe(OrderDto orderDto, KakaoJwtToken kakaoJwtToken, String url) {
    restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    headers.setBearerAuth(kakaoJwtToken.getAccessToken());

    String body = "template_object=" + createTemplateObject(orderDto);
    HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
    String response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class)
      .getBody();
  }

  public String createTemplateObject(OrderDto orderDto) {
    StringBuilder itemsBuilder = new StringBuilder();
    OptionDto optionDto = orderDto.getOptionDto();
    itemsBuilder.append("""
      {"item":"%s","item_op":"%d개"},""".formatted(optionDto.getName(), optionDto.getQuantity()));

    if (itemsBuilder.length() > 0) {
      itemsBuilder.setLength(itemsBuilder.length() - 1);
    }

    return """
      {
        "object_type" : "feed",
        "content" : {
          "title" : "주문이 완료되었습니다.",
          "image_url" : "imageUrl",
          "description" : "상세설명입니다.",
          "link" : {
          }
        },
        "item_content": {
          "items": [%s]
        }
      }
      """.formatted(itemsBuilder.toString());
  }
}
