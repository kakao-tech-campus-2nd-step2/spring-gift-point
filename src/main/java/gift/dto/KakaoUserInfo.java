package gift.dto;

public record KakaoUserInfo(Long id, String connected_at, KakaoAccount kakao_account) {
    public static class KakaoAccount {
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
    }
}
