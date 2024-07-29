package gift.product.model;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn
    private SnsMember snsMember;

    public Member() {}

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member(SnsMember snsMember) {
        this.email = "";
        this.password = "";
        this.snsMember = snsMember;
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

    public SnsMember getSnsMember() {
        return snsMember;
    }
}
