package gift.service.dto;

public record LoginDto(
        String accessToken,
        String name
) {
    public static LoginDto of(String accessToken, String name) {
        return new LoginDto(accessToken, name);
    }
}
