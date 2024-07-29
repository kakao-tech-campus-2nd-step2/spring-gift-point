package gift.kakao.login.dto;

import gift.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@DiscriminatorValue("kakao_user")
public class KakaoUser extends User {
    @Column(length = 512)
    private String token;

    public KakaoUser() {

    }
    public KakaoUser(String token) {
        this.token = token;
    }

    public KakaoUser(String email, String password, String token) {
        super(email, password);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
