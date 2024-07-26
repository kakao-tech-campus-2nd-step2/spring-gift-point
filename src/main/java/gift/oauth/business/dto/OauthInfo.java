package gift.oauth.business.dto;

import java.time.ZonedDateTime;

public class OauthInfo {
    public record Kakao(
        long id,
        ZonedDateTime connected_at,
        KakaoAccount kakao_account
    ) {
        public record KakaoAccount(
            boolean has_email,
            boolean email_needs_agreement,
            boolean is_email_valid,
            boolean is_email_verified,
            String email
        ) {}
    }



}
