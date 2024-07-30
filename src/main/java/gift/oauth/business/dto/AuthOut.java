package gift.oauth.business.dto;

public class AuthOut {
    public record Init(
        String email,
        String accessToken
    ) {

    }

}
