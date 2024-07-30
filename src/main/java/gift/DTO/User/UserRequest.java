package gift.DTO.User;

public class UserRequest {
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
