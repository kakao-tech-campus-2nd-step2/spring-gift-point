package gift.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Column(nullable = true)
    private final String name;

    @Column(nullable = false, unique = true)
    private final String email;

    @Column(nullable = false)
    private final String password;

    @Column(nullable = false, columnDefinition = "int default 0")
    private final int point;

    protected Member() {
        this(null, null, null, null, 0);
    }

    public Member(String email, String password) {
        this(null, null, email, password, 0);
    }

    public Member(String email, String password, int count) {
        this(null, null, email, password, count);
    }

    public Member(Long id, String email, String password) {
        this(id, null, email, password, 0);
    }

    public Member(Long id, String name, String email, String password) {
        this(id, name, email, password, 0);
    }

    public Member(Long id, String email, String password, int point) {
        this(id, null, email, password, point);
    }

    public Member(Long id, String name, String email, String password, int point) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.point = point;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getPoint() {
        return point;
    }
}
