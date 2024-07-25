package gift.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

import static gift.constant.Message.EMAIL_PATTERN_ERROR_MSG;
import static gift.constant.Message.REQUIRED_FIELD_MSG;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = EMAIL_PATTERN_ERROR_MSG)
    @NotBlank(message = REQUIRED_FIELD_MSG)
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = REQUIRED_FIELD_MSG)
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wish> wishes;

    public Member() {
    }

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
