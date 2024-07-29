package gift.member.business.dto;

public record JwtToken(
    String accessToken,
    String refreshToken
) {

}
