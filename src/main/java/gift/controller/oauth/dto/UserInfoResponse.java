package gift.controller.oauth.dto;

import java.time.LocalDateTime;

public record UserInfoResponse(Long id, LocalDateTime connected_at, KakaoAccount kakao_account) {

}
