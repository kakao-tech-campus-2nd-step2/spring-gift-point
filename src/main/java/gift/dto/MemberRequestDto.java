package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "멤버 요청 DTO")
public class MemberRequestDto {
    @Schema(description = "멤버 고유 id ")
    private Long id;
    @Email
    @Schema(description = "멤버 email ")
    private String email;
    @Schema(description = "멤버 비밀번호")
    private String password;

    public MemberRequestDto() {
    }

    public MemberRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MemberRequestDto( String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
