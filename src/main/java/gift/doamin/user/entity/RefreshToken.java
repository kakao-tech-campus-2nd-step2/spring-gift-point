package gift.doamin.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @OneToOne(optional = false)
    private User user;

    protected RefreshToken() {
    }

    public RefreshToken(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
