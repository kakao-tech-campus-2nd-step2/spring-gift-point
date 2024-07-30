package gift.member;

public class UserDTO {
    private Long userId;
    private String username;

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
