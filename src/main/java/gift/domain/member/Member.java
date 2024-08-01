package gift.domain.member;

import gift.domain.Wish;
import gift.exception.ErrorCode;
import gift.exception.GiftException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false)
    private Integer point;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private SocialAccount socialAccount;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    public Member() {
    }

    @PrePersist
    private void prePersist() {
        this.point = this.point == null ? 0 : this.point;
        this.role = this.role == null ? MemberRole.MEMBER : this.role;
    }

    public Long getId() {
        return id;
    }

    public MemberRole getRole() {
        return role;
    }

    public SocialAccount getSocialAccount() {
        return socialAccount;
    }

    public Integer getPoint() {
        return point;
    }

    public Member(Long id, String email, String password, MemberRole role, SocialAccount socialAccount, Integer point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.socialAccount = socialAccount;
        this.point = point;
    }

    public void setSocialAccount(SocialAccount socialAccount) {
        socialAccount.setMember(this);
        this.socialAccount = socialAccount;
    }

    public void subtractPoint(Integer point) {
        if (point == null || point < 0) {
            throw new GiftException(ErrorCode.INVALID_POINT);
        }
        if (this.point < point) {
            throw new GiftException(ErrorCode.INSUFFICIENT_POINT);
        }
        this.point -= point;
    }

    public void earnPoint(int price, Long quantity) {
        Long totalOrderPrice = price * quantity;
        this.point += (int) Math.round(totalOrderPrice * 0.1);
    }

    public static class MemberBuilder {
        private Long id;
        private String email;
        private String password;
        private MemberRole role;
        private SocialAccount socialAccount;
        private Integer point;

        public MemberBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder role(MemberRole role) {
            this.role = role;
            return this;
        }

        public MemberBuilder socialAccount(SocialAccount socialAccount) {
            this.socialAccount = socialAccount;
            return this;
        }

        public MemberBuilder point(Integer point) {
            this.point = point;
            return this;
        }

        public Member build() {
            return new Member(id, email, password, role, socialAccount, point);
        }
    }

}
