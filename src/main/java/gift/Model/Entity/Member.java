package gift.Model.Entity;

import gift.Model.Value.AccessToken;
import gift.Model.Value.Email;
import gift.Model.Value.Password;
import jakarta.persistence.*;

import java.util.Optional;

@Entity
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "email", nullable = false, unique = true))
    private Email email;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "password", nullable = false))
    private Password password;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "access_token"))
    private AccessToken accessToken;

    protected Member() {}

    public Member(Email email, Password password) {
        this.email = email;
        this.password = password;
        this.accessToken = null;
    }

    public Member(String email, String password) {
        this(new Email(email), new Password(password));
    }

    public Long getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public Optional<AccessToken>  getAccessToken() {
        return Optional.ofNullable(accessToken);
    }

    public void update(Email email, Password password){
        this.email = email;
        this.password = password;
    }

    public void update(String email, String password) {
        update(new Email(email), new Password(password));
    }

    public void updateAccessToken(String accessToken){
        this.accessToken = new AccessToken(accessToken);
    }
}
