package gift.member.presentation.request;

import gift.member.application.command.MemberJoinCommand;

public record MemberJoinRequest(
        String email,
        String password
        ) {
    public MemberJoinCommand toCommand() {
        return new MemberJoinCommand(
                email,
                password
        );
    }
}
