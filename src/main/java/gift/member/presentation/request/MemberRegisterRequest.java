package gift.member.presentation.request;

import gift.member.application.command.MemberRegisterCommand;

public record MemberRegisterRequest(
        String email,
        String password
        ) {
    public MemberRegisterCommand toCommand() {
        return new MemberRegisterCommand(
                email,
                password
        );
    }
}
