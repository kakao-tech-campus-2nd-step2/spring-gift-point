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
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank()
    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String kakaoAccessToken;

    @Column
    private int point;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishList;

    protected Member() {
    }

    public Member(String email, String password) {
        this(null, email, password, null, 0);
    }

    public Member(Long id, String email, String password) {
        this(id, email, password, null, 0);
    }

    public Member(String email, String password, String kakaoAccessToken) {
        this(null, email, password, kakaoAccessToken, 0);
    }

    public Member(Long id, String email, String password, String kakaoAccessToken, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.kakaoAccessToken = kakaoAccessToken;
        this.wishList = new ArrayList<>();
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

    public int getPoint() {
        return point;
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

    public void updatePoint(int point) {
        this.point = point;
    }

    public void addWish(Wish wish) {
        this.wishList.add(wish);
    }

    public void removeWish(Wish wish) {
        this.wishList.remove(wish);
    }
}
