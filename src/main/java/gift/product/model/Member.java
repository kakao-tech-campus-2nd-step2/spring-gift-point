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

    protected Member() {
        this(null, null, null, null);
    }

    public Member(String email, String password) {
        this(null, null, email, password);
    }

    public Member(String name, String email, String password) {
        this(null, name, email, password);
    }

    public Member(Long id, String email, String password) {
        this(id, null, email, password);
    }

    public Member(Long memberId, String name, String email, String password) {
        this.id = memberId;
        this.name = name;
        this.email = email;
        this.password = password;
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
}
