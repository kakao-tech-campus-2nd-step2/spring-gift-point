package gift.dto;

public class MemberRegisterDto {

    public final String email;
    public final String password;

    public MemberRegisterDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
