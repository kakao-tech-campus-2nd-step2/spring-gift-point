package gift.member.dto;

import gift.member.entity.Member;

public record MemberReqDto(
        String email,
        String password
) {

    public Member toEntity() {

        return new Member(
                email,
                password
        );
    }
}
