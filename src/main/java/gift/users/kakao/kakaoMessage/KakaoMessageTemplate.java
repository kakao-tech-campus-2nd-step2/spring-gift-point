package gift.users.kakao.kakaoMessage;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoMessageTemplate(String objectType, Content content, Commerce commerce, List<Button> buttons) {

}
