package gift.member.business.dto;

import gift.member.persistence.entity.Member;

public record MemberRegisterDto(
    String email,
    String password
) {

    public Member toMember() {
        return new Member(email, password);
    }
}
