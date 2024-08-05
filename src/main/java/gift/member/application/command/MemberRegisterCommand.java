package gift.member.application.command;

import gift.member.domain.Member;

import static gift.member.domain.OauthProvider.COMMON;

public record MemberRegisterCommand(
        String email,
        String password
) {
    public Member toMember() {
        return new Member(email, password, COMMON);
    }
}
