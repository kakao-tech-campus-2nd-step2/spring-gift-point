package gift.model;

import gift.common.enums.SocialType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity(name = "users")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SocialType socialType;

    protected User() {
    }

    public User(String password, String email, SocialType socialType) {
        this.password = password;
        this.email = email;
        this.socialType = socialType;
    }

    public Long getId() {return id;}

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public SocialType getSocialType() {
        return socialType;
    }

    public boolean checkSocialType(SocialType socialType) {
        return this.socialType == socialType;
    }
}
