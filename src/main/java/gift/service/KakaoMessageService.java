package gift.service;

import gift.config.KakaoProperties;
import gift.model.Order;
import gift.model.Product;
import gift.repository.OptionRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class KakaoMessageService {
    public static final String TEMPLATE_ID = "110454";

    private final RestTemplate restTemplate = new RestTemplate();
    private final KakaoLoginService kakaoLoginService;
    private final OptionRepository optionRepository;
    private final KakaoProperties properties;

    public KakaoMessageService(KakaoLoginService kakaoLoginService,
                               OptionRepository optionRepository,
                               KakaoProperties properties) {
        this.kakaoLoginService = kakaoLoginService;
        this.optionRepository = optionRepository;
        this.properties = properties;
    }

    public void sendMessageToKakao(Order order, Long memberId) {
        String accessToken = kakaoLoginService.getAccessTokenForMember(memberId);
        String url = properties.apiUrl() + properties.messageSendUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        Product product = optionRepository.findProductByOptionId(order.getOption().getId())
                .orElseThrow(() -> new NoSuchElementException("해당 옵션의 상품이 존재하지 않습니다."));
        body.add("template_id", TEMPLATE_ID);
        body.add("template_args", String.format(
                "{\"TITLE\":\"%s\",\"PRODUCT\":\"%s\",\"OPTION\":\"%s\",\"QUANTITY\":\"%d\",\"MESSAGE\":\"%s\"}",
                "주문이 완료되었습니다.",
                product.getName(),
                order.getOption().getName(),
                order.getQuantity(),
                order.getMessage()
        ));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(url, requestEntity, Map.class);
        } catch (Exception e) {
            System.out.println("카카오 메시지 전송 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("카카오 메시지 전송 중 오류 발생", e);
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("카카오 메시지 전송 실패: " + responseEntity.getStatusCode());
        }

        Map<String, Object> response = responseEntity.getBody();
        if (response == null || !response.get("result_code").equals(0)) {
            throw new RuntimeException("카카오 메시지 전송 실패: " + response);
        }
    }
}
