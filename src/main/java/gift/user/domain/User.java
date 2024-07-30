package gift.user.domain;

import gift.product.domain.WishList;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;


    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String name;

    @Column(nullable = true, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;


    @Column(nullable = true)
    private String profileImageUrl;


    @Column(nullable = true)
    private Long kakaoId;

    private LocalDateTime createdAt;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private WishList wishList;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.loginType = LoginType.NORMAL;
    }

    public User(String name, String profileImageUrl, Long kakaoId) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.kakaoId = kakaoId;
        this.loginType = LoginType.KAKAO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public WishList getWishLists() {
        return wishList;
    }

    public Role getRole() {
        return role;
    }

}
