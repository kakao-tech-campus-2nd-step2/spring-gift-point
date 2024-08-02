package gift.domain.member.dto;

public record MemberLoginResponse(
    String email,
    String token
) {
}
