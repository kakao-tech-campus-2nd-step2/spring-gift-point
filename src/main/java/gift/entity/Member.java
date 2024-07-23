package gift.entity;

import jakarta.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    private Member(Builder builder) {
        this.id = builder.id;
        this.email = builder.email;
        this.password = builder.password;
    }

    public Member() {}

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void update(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static class Builder {
        private Long id;
        private String email;
        private String password;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public Member build() {
            return new Member(this);
        }
    }
}

