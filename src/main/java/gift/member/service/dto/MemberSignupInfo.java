package gift.member.service.dto;

public record MemberSignupInfo(
        Long id,
        String token
) {
    public static MemberSignupInfo of(final Long id, final String token) {
        return new MemberSignupInfo(id, token);
    }
}