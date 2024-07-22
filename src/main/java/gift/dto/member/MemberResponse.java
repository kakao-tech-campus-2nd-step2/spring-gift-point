package gift.dto.member;

public record MemberResponse(
    Long id,
    String email,
    String token
) {

}
