package gift.dto.response;

import gift.domain.Member;

public record MemberResponseDto(Long id, String email, String password) {

    public static MemberResponseDto of(Long id, String email, String password) {
        return new MemberResponseDto(id, email, password);
    }

    public static MemberResponseDto from(Member member) {
        return new MemberResponseDto(member.getId(), member.getEmail(), member.getPassword());
    }

}