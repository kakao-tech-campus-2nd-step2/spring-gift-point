package gift.member.presentation.request;

import gift.member.application.command.MemberPasswordUpdateCommand;

public record MemberPasswordUpdateRequest(
        String password
) {
    public MemberPasswordUpdateCommand toCommand() {
        return new MemberPasswordUpdateCommand(
                password
        );
    }
}
