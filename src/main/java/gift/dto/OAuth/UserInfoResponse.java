package gift.dto.OAuth;

public class UserInfoResponse {
    public record Info(
            String email,
            String name
    ){

    }
}
