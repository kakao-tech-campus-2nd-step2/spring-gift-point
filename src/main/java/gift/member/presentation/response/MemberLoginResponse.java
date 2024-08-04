package gift.member.presentation.response;

public record MemberLoginResponse (
        String email,
        String token
){
    public static MemberLoginResponse of(String email, String token) {
        return new MemberLoginResponse(email, token);
    }
}
