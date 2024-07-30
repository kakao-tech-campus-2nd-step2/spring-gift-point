package gift.client.requestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.KakaoMessageTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KakaoMessageRequestBodyGenerator {
    private static final Logger log = LoggerFactory.getLogger(KakaoMessageRequestBodyGenerator.class);
    private final String message;
    private final ObjectMapper objectMapper;

    public KakaoMessageRequestBodyGenerator(String message, ObjectMapper objectMapper) {
        this.message = message;
        this.objectMapper = objectMapper;
    }

    public MultiValueMap<String, String> toMultiValueMap() {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        KakaoMessageTemplate template = new KakaoMessageTemplate(message);

        try {
            param.add("template_object", objectMapper.writeValueAsString(template));
        } catch (JsonProcessingException e) {
            log.warn("ObjectMapper가 JSON 파싱에 실패");
        }

        return param;
    }
}
