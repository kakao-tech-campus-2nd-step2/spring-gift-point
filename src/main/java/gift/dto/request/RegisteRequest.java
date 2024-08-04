package gift.dto.request;

public class RegisteRequest {
    
    private String email;
    private String password;

    public RegisteRequest(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}
