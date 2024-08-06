package gift.model;

import jakarta.persistence.*;

@Entity(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String accessToken;
    @Embedded
    private Point point;

    public Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = new Point(0);
    }

    public Member(String email, String password, String accessToken) {
        this.email = email;
        this.password = password;
        this.accessToken = accessToken;
        this.point = new Point(0);
    }

    public Member(Long id, String email, String password, Point point) {
        this.id = id;
        this.email = email;
        this.password = password;
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

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Point getPoint() {
        return point;
    }


}
