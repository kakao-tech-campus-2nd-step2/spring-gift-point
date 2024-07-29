package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import org.springframework.cglib.core.Local;

@Entity
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String accesstoken;

    @Column(nullable = false)
    String refreshtoken;

    @Column(nullable = false)
    int accesstokentime;

    @Column(nullable = false)
    int refreshtokentime;

    @Column(nullable = false)
    LocalDateTime createtime;

    public Token(String email, String accesstoken, String refreshtoken, int accesstokentime,
        int refreshtokentime, LocalDateTime createtime) {
        this.email = email;
        this.accesstoken = accesstoken;
        this.refreshtoken = refreshtoken;
        this.accesstokentime = accesstokentime;
        this.refreshtokentime = refreshtokentime;
        this.createtime = createtime;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return accesstoken;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public Token() {

    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatetime() {
        return createtime;
    }

    public int getAccesstokentime() {
        return accesstokentime;
    }

    public int getRefreshtokentime() {
        return refreshtokentime;
    }

    public void updateAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }
}
