package gift.dto;

import gift.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "멤버 응답 DTO")
public class MemberResponseDto {

    @Schema(description = "멤버 고유 id ")
    private final Long id;
    @Email
    @Schema(description = "멤버 email ")
    private final String email;
    @Schema(description = "멤버 토큰")
    private String token;

    public MemberResponseDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public MemberResponseDto(Member actualMember, String token) {
        this.id = actualMember.getId();
        this.email = actualMember.getEmail();
        this.token = actualMember.getToken();
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}

