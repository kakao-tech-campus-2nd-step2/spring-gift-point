package gift.domain.Member;

public record MemberRequest(
        String id,
        String password,
        String name
) {
}
