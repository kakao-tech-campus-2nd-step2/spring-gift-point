package gift.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "member", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int points;

    @JsonIgnore
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes;

    protected Member() {
    }

    private Member(MemberBuilder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
        this.points = builder.points;
        this.wishes = builder.wishes;
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

    public int getPoints() {
        return points;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public Member updatePoints(int points) {
        return new MemberBuilder()
            .id(this.id)
            .email(this.email)
            .password(this.password)
            .points(points)
            .wishes(this.wishes)
            .build();
    }

    public static class MemberBuilder {
        private Long id;
        private String email;
        private String password;
        private int points;
        private List<Wish> wishes;

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

        public MemberBuilder points(int points) {
            this.points = points;
            return this;
        }

        public MemberBuilder wishes(List<Wish> wishes) {
            this.wishes = wishes;
            return this;
        }

        public Member build() {
            return new Member(this);
        }
    }
}
