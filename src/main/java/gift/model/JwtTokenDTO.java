package gift.model;

public record JwtTokenDTO(
    String tokenType,
    String accessToken
) {

}
