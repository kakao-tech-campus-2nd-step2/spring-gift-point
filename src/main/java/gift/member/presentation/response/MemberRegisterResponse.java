package gift.member.presentation.response;

public record MemberRegisterResponse (
        String email,
        String token
){
    public static MemberRegisterResponse of(String email, String token) {
        return new MemberRegisterResponse(email, token);
    }
}
