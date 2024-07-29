package gift.dto;

import gift.vo.Member;

public record MemberDto (
        Long id,
        String email,
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
