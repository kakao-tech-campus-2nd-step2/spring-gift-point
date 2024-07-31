package gift.controller.dto.response;

public record LoginResponse(String name) {
    public static LoginResponse of(String name) {
        return new LoginResponse(name);
    }
}
