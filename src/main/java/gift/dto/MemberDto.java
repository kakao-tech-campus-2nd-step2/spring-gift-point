package gift.dto;

public class MemberDto {
    
    private long id;
    private String password;
    private String email;
    private String role;

    public MemberDto() {
    }

    public MemberDto(long id, String password, String email, String role) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }
}
