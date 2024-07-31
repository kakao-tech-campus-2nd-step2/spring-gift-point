package gift.member.service.command;

import gift.member.domain.Member;

public record MemberInfoCommand(
        String username,
        String password,
        boolean withOAuth
) {
    public Member toEntity() {
        return new Member(username, password, withOAuth);
    }
}
