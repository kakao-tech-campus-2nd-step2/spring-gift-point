package gift.dto;

public class LoginResponseDTO {

    private String accessToken;
    private String email;

    public LoginResponseDTO(String accessToken, String email) {
        this.accessToken = accessToken;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
