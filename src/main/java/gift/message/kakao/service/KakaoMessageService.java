package gift.message.kakao.service;

import static gift.global.utility.MultiValueMapConverter.bodyConvert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gift.global.client.ServerClient;
import gift.global.utility.MultiValueMapConverter;
import gift.message.kakao.dto.KakaoMessageForMeResponseDto;
import gift.order.kakao.dto.MessageRequestDto;
import gift.order.kakao.model.Link;
import gift.order.kakao.model.TemplateObject;
import gift.permission.user.service.UserService;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class KakaoMessageService {

    private final UserService userService;
    private final ServerClient serverClient;
    private static final String SEND_ME_MESSAGE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private static final String TEXT_TYPE_MESSAGE = "text";
    private static final String TEXT_TYPE_BUTTON_TITLE = "굉장히 뜬금없는 버튼";
    private static final String BUTTON_LINK_URL = "https://www.mofa.go.kr/www/nation/m_3458/view.do?seq=105";

    @Autowired
    public KakaoMessageService(UserService userService, ServerClient serverClient) {
        this.userService = userService;
        this.serverClient = serverClient;
    }

    // 카카오톡 나에게 보내기
    public void sendTextMessageForMe(long userId, String message) {
        // 액세스 토큰을 보내야 하는데 원래라면 input으로 가져왔겠지만, 지금은 카카오의 액세스 토큰을 서비스의 리프레시 토큰으로 사용하고 있으므로 User table에서 가져오도록 합니다.
        var refreshToken = userService.getRefreshToken(userId);
        Consumer<HttpHeaders> headersConsumer = headers -> {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBearerAuth(refreshToken);
        };

        // body 준비
        var link = new Link(BUTTON_LINK_URL, BUTTON_LINK_URL);
        var templateObject = new TemplateObject(TEXT_TYPE_MESSAGE, message, link,
            TEXT_TYPE_BUTTON_TITLE);
//        var body = bodyConvert(new MessageRequestDto(templateObject));
        // valueToTree라는 좋은 메서드를 찾았습니다..
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        var jsonDto = objectMapper.valueToTree(templateObject);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", jsonDto.toString());

        // 메시지 전송
        var response = serverClient.postRequest(SEND_ME_MESSAGE_URL, body, headersConsumer,
            KakaoMessageForMeResponseDto.class).getBody();

        // 실패 시 예외
        if (response.resultCode() != 0) {
            throw new IllegalArgumentException("메시지 전송에 실패했습니다.");
        }
    }
}
