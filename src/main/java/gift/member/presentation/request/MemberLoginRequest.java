package gift.member.presentation.request;

import gift.member.application.command.MemberLoginCommand;

public record MemberLoginRequest(
        String email,
        String password
) {
    public MemberLoginCommand toCommand() {
        return new MemberLoginCommand(
                email,
                password
        );
    }
}
