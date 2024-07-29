package gift.service;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.request.kakaomessage.KakaoLink;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;

@SpringBootTest
@ActiveProfiles("test")
class KakaoMessageServiceTest {

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("카카오톡 메시지 나에게 보내기 요청 JSON 변환 테스트")
    @Test
    void toMeMessageToJson() throws JsonProcessingException {
        LinkedMultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        KakaoLink link = KakaoLink.createLink();
        String message = "메시지입니다.";

        HashMap<String,Object> templateObject = new HashMap<>();
        templateObject.put("object_type", "text");
        templateObject.put("text", message);
        templateObject.put("link",  link);

        body.add("template_object", objectMapper.writeValueAsString(templateObject));
        System.out.println(body);
    }



}