package gift.member.util;

import gift.kakao.auth.dto.KakaoTokenResponse;
import gift.member.entity.KakaoTokenInfo;

import java.time.LocalDateTime;

public class KakaoTokenMapper {

    public static KakaoTokenInfo toTokenInfo(KakaoTokenResponse response) {
        return new KakaoTokenInfo(
                response.accessToken(),
                LocalDateTime.now()
                             .plusSeconds(response.expiresIn()),
                response.refreshToken()
        );
    }

}
