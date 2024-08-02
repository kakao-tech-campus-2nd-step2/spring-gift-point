package gift.DTO.User;

import jakarta.validation.constraints.Email;

public class UserRequest {
    @Email
    String email;
    String password;

    public UserRequest(){

    }

    public UserRequest(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
