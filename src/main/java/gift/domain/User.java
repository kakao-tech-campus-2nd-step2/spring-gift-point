package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User extends BaseEntity{
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Boolean admin;
    @Column
    private String token;
    @Column(nullable = false)
    private int point;

    protected User(){
        super();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.admin = false;
        this.point = 0;
    }
    public Long getId() {
        return super.getId();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public String getToken() {
        return token;
    }

    public void insertToken(String token) {
        this.token = token;
    }

    public void updateEntity(String email, String password){
        this.email = email;
        this.password = password;
    }

    public void addPoint(int point){
        this.point += point;
    }

    public void usePoint(int point){
        if(this.point - point < 0)
            return;
        this.point -= point;
    }
}
