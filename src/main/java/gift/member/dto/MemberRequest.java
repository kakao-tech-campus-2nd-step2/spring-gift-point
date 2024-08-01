package gift.member.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberRequest {
    @Email
    private String email;
    private String password;
    private String kakaoId;

    public MemberRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MemberRequest(String email, String password, String kakaoId) {
        this.email = email;
        this.password = password;
        this.kakaoId = kakaoId;
    }
}
