package gift.service;

public record UserDto(
    String accessToken,
    String name
) {

    public static UserDto from(String accessToken, String name) {
        return new UserDto(accessToken, name);
    }
}
