package gift.dto;

import gift.domain.member.Member;

public record MemberResponseDto(
        Long id,
        String name,
        String email,
        int point) {
    public MemberResponseDto(Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getPoint());
    }
}
