package gift.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class MemberDto {
    @Email
    private String email;
    private String password;
    private String kakaoId;

    public MemberDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MemberDto(String email, String password, String kakaoId) {
        this.email = email;
        this.password = password;
        this.kakaoId = kakaoId;
    }
}
