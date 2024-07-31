package gift.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member", uniqueConstraints = {
    @UniqueConstraint(name = "EMAIL_UNIQUE", columnNames = {"email"})})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private Long kakaoId;

    @Column
    private Boolean isKakaoMember;

    @OneToMany(mappedBy = "member",
               cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wishlist> wishes = new ArrayList<>();

    protected Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password, Long kakaoId) {
        this(email, password);
        this.kakaoId = kakaoId;
        this.isKakaoMember = Boolean.TRUE;
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

    public Long getKakaoId() {
        return kakaoId;
    }

    public Boolean isKakaoMember() {
        return isKakaoMember;
    }
}
