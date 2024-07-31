package gift.model.user;

public record AuthenticationRequest(
    String email,
    String password
) {

}
