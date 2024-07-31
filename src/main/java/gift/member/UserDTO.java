package gift.member;

public class UserDTO {
    private final Long userId;
    private final String username;

    public UserDTO(Long userId, String username){
        this.userId = userId;
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
