package gift.dto;

import gift.vo.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 DTO")
public record MemberDto (

        @Schema(description = "회원 ID")
        Long id,

        @Schema(description = "회원 이메일")
        String email,

        @Schema(description = "회원 비밀번호")
        String password
){
    public static MemberDto toMemberDto(Member member){
        return new MemberDto(
                member.getId(),
                member.getEmail(),
                member.getPassword()
        );
    }
}
