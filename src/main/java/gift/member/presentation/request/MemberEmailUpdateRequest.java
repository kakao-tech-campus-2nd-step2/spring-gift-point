package gift.member.presentation.request;

import gift.member.application.command.MemberEmailUpdateCommand;

public record MemberEmailUpdateRequest(
        String email
        ) {
    public MemberEmailUpdateCommand toCommand() {
        return new MemberEmailUpdateCommand(
                email
        );
    }
}
