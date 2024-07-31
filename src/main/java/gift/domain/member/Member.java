package gift.domain.member;

import gift.domain.Wish;
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

    private String password;

    private MemberRole role;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private SocialAccount socialAccount;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Wish> wishes = new ArrayList<>();

    public Member() {
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

    public Member(Long id, String email, String password, MemberRole role, SocialAccount socialAccount) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.socialAccount = socialAccount;
    }

    public void setSocialAccount(SocialAccount socialAccount) {
        socialAccount.setMember(this);
        this.socialAccount = socialAccount;
    }

    public static class MemberBuilder {
        private Long id;
        private String email;
        private String password;
        private MemberRole role;
        private SocialAccount socialAccount;

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

        public Member build() {
            return new Member(id, email, password, role, socialAccount);
        }
    }

}
