package gift.Model.Entity;

import gift.Model.Value.AccessToken;
import gift.Model.Value.Email;
import gift.Model.Value.Password;
import gift.Model.Value.Point;
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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "pont", nullable = false))
    private Point point;

    protected Member() {}

    public Member(Email email, Password password, Point point) {
        this.email = email;
        this.password = password;
        this.accessToken = null;
        this.point = point;
    }

    public Member(String email, String password, int point ) {
        this(new Email(email), new Password(password), new Point(point));
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

    public Point getPoint() {
        return point;
    }

    public void update(Email email, Password password, Point point){
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public void update(String email, String password, int point) {
        update(new Email(email), new Password(password), new Point(point));
    }

    public void updateAccessToken(String accessToken){
        this.accessToken = new AccessToken(accessToken);
    }

    public void subtractPoint(int point){
        this.point.subtract(point);
    }

    public void addPoint(int point){
        this.point.add(point);
    }
}
