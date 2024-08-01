package gift.dto;

public class MemberResponseDto {
    private String email;
    private String token;

    public MemberResponseDto(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
