package gift.domain;


import gift.classes.Exceptions.OptionException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "point")
    private int point;

    protected Member() {
    }

    public Member(Long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Member(Long id, String email, String password, Role role, int point) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.point = point;
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

    public Role getRole() {
        return role;
    }

    public int getPoint() {
        return point;
    }

    public void decrementPoint(int point){
        if (this.point <= 0) {
            throw new IllegalArgumentException("The point to be deducted must be greater than zero.");
        }
        if (this.point >= point) {
            this.point -= point;
        } else {
            throw new IllegalArgumentException("Deduction failed due to insufficient point.");
        }
    }

    public void earnPoint(int price, int quantity){
        this.point += (int) (price*quantity*0.3);
    }
}
