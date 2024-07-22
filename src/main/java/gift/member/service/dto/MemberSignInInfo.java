package gift.member.service.dto;

public record MemberSignInInfo(
        String token
) {
    public static MemberSignInInfo of(final String token) {
        return new MemberSignInInfo(token);
    }
}
