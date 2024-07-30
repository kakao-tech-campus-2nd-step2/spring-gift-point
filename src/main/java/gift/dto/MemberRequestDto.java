package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "멤버 요청 DTO")
public class MemberRequestDto {
    @Schema(description = "멤버 고유 id ")
    private final Long id;
    @Email
    @Schema(description = "멤버 email ")
    private String email;
    @Schema(description = "멤버 비밀번호")
    private final String password;


    public MemberRequestDto(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public MemberRequestDto(String email, String password) {
        this(null, email, password);
    }

    public MemberRequestDto(Long id, String password) {
        this.id = id;
        this.password = password;
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
