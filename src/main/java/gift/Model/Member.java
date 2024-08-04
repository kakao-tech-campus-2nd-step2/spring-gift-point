package gift.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.beans.ConstructorProperties;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "accessToken")
    private String accessToken;
    @Column(name = "point")
    private int point;

    protected Member(){

    }

    @ConstructorProperties({"id","email","password","accessToken","point"})
    public Member(Long id, String email, String password, String accessToken,int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getPoint() {
        return point;
    }
    public int usePoint(int use){
        return this.point-=use;
    }
    public int chargePoint(int charge){
        return this.point+=charge;
    }
}
