package gift.DTO.User;

import gift.domain.User;

public class UserResponse {
    Long id;
    String userId;
    String email;
    String password;
    String token;

    public UserResponse(){

    }

    public UserResponse(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.token = user.getToken();
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getToken(){
        return token;
    }
}
