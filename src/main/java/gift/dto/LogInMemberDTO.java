package gift.dto;

public class LogInMemberDTO {
    private String email;
    private String password;

    public LogInMemberDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
