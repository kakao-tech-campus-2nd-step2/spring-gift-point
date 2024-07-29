package gift.users.kakao.kakaoMessage;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Content(String title, String imageUrl, int imageWidth, int imageHeight,
                      String description, Link link) {

}
