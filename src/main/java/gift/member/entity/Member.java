package gift.member.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import gift.member.dto.MemberReqDto;
import gift.wishlist.entity.WishList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String kakaoAccessToken;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishList> wishLists = new ArrayList<>();

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    protected Member() {
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

    public List<WishList> getWishLists() {
        return wishLists;
    }

    public void changeKakaoAccessToken(String kakaoAccessToken) {
        this.kakaoAccessToken = kakaoAccessToken;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }

    public void update(MemberReqDto memberReqDto) {
        this.email = memberReqDto.email();
        this.password = memberReqDto.password();
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void deleteWishList(WishList findWishList) {
        this.wishLists.remove(findWishList);
    }
}
