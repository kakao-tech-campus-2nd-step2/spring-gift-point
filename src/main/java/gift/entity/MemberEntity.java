package gift.entity;

import gift.domain.Member;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "members")
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    private List<WishListEntity> wishListEntities;

    private String kakaoToken;

    public MemberEntity() {

    }

    public MemberEntity(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public MemberEntity(String email, String password, String kakaoToken) {
        this.email = email;
        this.password = password;
        this.kakaoToken = kakaoToken;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<WishListEntity> getWishListEntities() {
        return wishListEntities;
    }

    public String getKakaoToken() {
        return kakaoToken;
    }

    public void setKakaoToken(String kakaoToken) {
        this.kakaoToken = kakaoToken;
    }

    public void removeWishListHasProductEntity(ProductEntity productEntity) {
        wishListEntities.removeIf(wishList -> wishList.getProductEntity().equals(productEntity));
    }

    public static Member toDto(MemberEntity memberEntity) {
        return new Member(memberEntity.getId(), memberEntity.getEmail(),
            memberEntity.getPassword());
    }

}
