package gift.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    private String access_token;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes =  new ArrayList<>();

    public Member(String mail, String password, String access_token) {
        this.email = mail;
        this.password = password;
        this.access_token = access_token;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return user_id;
    }

    public List<Wish> getWishes() {
        return this.wishes;
    }

    public String getAccessToken() {
        return access_token;
    }
}
