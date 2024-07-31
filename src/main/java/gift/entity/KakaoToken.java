package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "kakao_token")
public class KakaoToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    private int expiresIn;

    public KakaoToken(String email, String accessToken, String refreshToken, int expiresIn){
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken(){
        return accessToken;
    }

    public String getRefreshToken(){
        return refreshToken;
    }

    public boolean isExpired(){
        
        //if(expiresIn > duration){ 
        // return true;
        // }

        return false;
    }

    public int getExpiresIn(){
        return expiresIn;
    }
}
