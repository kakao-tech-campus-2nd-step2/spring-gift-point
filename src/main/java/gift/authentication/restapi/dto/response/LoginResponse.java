package gift.authentication.restapi.dto.response;

import gift.core.domain.authentication.Token;

public record LoginResponse(
        String token
) {
    public static LoginResponse of(Token token) {
        return new LoginResponse(token.value());
    }
}
