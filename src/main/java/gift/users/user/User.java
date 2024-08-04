package gift.users.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "users")
@Entity
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name = "sns_id")
    private String snsId;
    private String sns;
    private int points = 0;

    public User() {
    }

    public User(String snsId, String sns) {
        this.snsId = snsId;
        this.sns = sns;
    }

    public User(Long id, String email, String password, String sns, int points) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.sns = sns;
        this.points = points;
    }

    public User(String email, String password, String sns) {
        this.email = email;
        this.password = password;
        this.sns = sns;
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

    public String getSns(){return sns;}

    public String getSnsId(){return snsId;}

    public int getPoints(){return points;}

    public void addPoints(int points){this.points += points;}

    public void usePoints(int points){this.points -= points;}
}
