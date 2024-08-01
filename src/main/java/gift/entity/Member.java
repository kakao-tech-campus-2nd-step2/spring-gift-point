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

    public Member() {}

    public Member(String email, String password, String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public Member(int id, String email, String password, String token) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public Member(String email, String token){
        this.email = email;
        this.token = token;
    }

    public String getToken() { return this.token; }

    public int getId() { return this.id; }
}
