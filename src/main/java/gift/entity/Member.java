package gift.entity;

import jakarta.persistence.*;

@Entity
@Table(name ="member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "password")
    private String password;

    @Column(name = "point")
    private int point;

    public Member() {}

    public Member(String email, String password, String token) {
        this.email = email;
        this.password = password;
        this.token = token;
        this.point = 0;
    }

    public Member(int id, String email, String password, String token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.token = token;
        this.point = 0;
    }

    public Member(String email, String token){
        this.email = email;
        this.token = token;
        this.point = 0;
    }

    public String getToken() { return this.token; }

    public int getId() { return this.id; }

    public int getPoint() { return this.point; }
}
