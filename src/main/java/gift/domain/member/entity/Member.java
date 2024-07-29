package gift.domain.member.entity;

import gift.domain.wishlist.entity.Wish;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String kakaoAccessToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishList;

    protected Member() {
    }

    public Member(String email, String password) {
        this(null, email, password, null);
    }

    public Member(Long id, String email, String password) {
        this(id, email, password, null);
    }

    public Member(String email, String password, String kakaoAccessToken) {
        this(null, email, password, kakaoAccessToken);
    }

    public Member(Long id, String email, String password, String kakaoAccessToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.kakaoAccessToken = kakaoAccessToken;
        this.wishList = new ArrayList<>();
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
        return kakaoAccessToken;
    }

    public void updateKakaoAccessToken(String accessToken) {
        this.kakaoAccessToken = accessToken;
    }

    public void addWish(Wish wish) {
        this.wishList.add(wish);
    }

    public void removeWish(Wish wish) {
        this.wishList.remove(wish);
    }
}
