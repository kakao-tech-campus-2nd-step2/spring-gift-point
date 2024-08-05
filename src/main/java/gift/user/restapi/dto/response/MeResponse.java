package gift.user.restapi.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.user.service.UserInfoDto;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MeResponse(
        Long id,
        String email,
        Long remainPoint
){
    public static MeResponse of(UserInfoDto userInfo) {
        return new MeResponse(userInfo.id(), userInfo.email(), userInfo.remainPoint());
    }
}
