package gift.member.application.command;

public record MemberLoginCommand(
        String email,
        String password
) {
}
