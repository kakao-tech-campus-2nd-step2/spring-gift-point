package gift.domain;

public record MemberRequest(
        String id,
        String password,
        String name
) {
}
