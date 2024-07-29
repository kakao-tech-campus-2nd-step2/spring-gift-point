package gift.dto;

public class MemberLoginDto {

    public final String email;
    public final String password;

    public MemberLoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
