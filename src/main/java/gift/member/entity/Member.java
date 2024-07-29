package gift.member.entity;

import gift.member.dto.MemberDto;
import gift.wishlist.entity.Wish;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member")
    private final List<Wish> wishList = new ArrayList<>();

    @Embedded
    private KakaoTokenInfo kakaoTokenInfo;

    protected Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email,
                  String password,
                  KakaoTokenInfo kakaoTokenInfo) {
        this.email = email;
        this.password = password;
        this.kakaoTokenInfo = kakaoTokenInfo;
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

    public List<Wish> getWishList() {
        return wishList;
    }

    public String getKakaoAccessToken() {
        return kakaoTokenInfo.getKakaoAccessToken();
    }

    public String getKakaoRefreshToken() {
        return kakaoTokenInfo.getKakaoRefreshToken();
    }

    public void update(MemberDto memberDto) {
        email = memberDto.email();
        password = memberDto.password();
    }

    public void refreshKakaoTokens(KakaoTokenInfo tokenInfo) {
        kakaoTokenInfo.refresh(tokenInfo);
    }

    public boolean isNotKakaoUser() {
        return kakaoTokenInfo == null;
    }

    public boolean isTokenExpired() {
        return kakaoTokenInfo.isExpired();
    }

}
