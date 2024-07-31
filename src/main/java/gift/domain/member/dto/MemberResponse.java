package gift.domain.member.dto;

import gift.domain.member.entity.Member;

public record MemberResponse(
    Long id,
    String name,
    String email,
    String password,
    String role,
    String authProvider)
{
    public static MemberResponse from(Member member) {
        return new MemberResponse(
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getPassword(),
            member.getRole().toString(),
            member.getAuthProvider().toString()
        );
    }
}
