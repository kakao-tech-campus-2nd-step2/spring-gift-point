package gift.dto;

public class LoginResponseDTO {

    private String token;  // 변경된 필드 이름
    private String email;

    public LoginResponseDTO(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
