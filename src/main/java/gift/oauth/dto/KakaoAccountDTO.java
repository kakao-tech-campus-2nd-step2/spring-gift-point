package gift.oauth.dto;

public class KakaoAccountDTO {

    private boolean hasEmail;
    private boolean emailNeedsAgreement;
    private boolean isEmailValid;
    private boolean isEmailVerfied;
    private String email;

    public String getEmail() {
        return email;
    }
}