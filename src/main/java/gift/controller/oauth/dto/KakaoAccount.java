package gift.controller.oauth.dto;

public record KakaoAccount
    (
    boolean has_email,
    boolean email_needs_agreement,
    boolean is_email_valid,
    boolean is_email_verified,
    String email
    ) {

}
