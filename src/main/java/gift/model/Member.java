package gift.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "유효한 이메일 주소여야 합니다.")
    @NotBlank(message = "이메일은 공백이 될 수 없습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    private String password;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WishList> wishLists = new HashSet<>();

    protected Member() {}

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

    // 현재 이메일과 비밀번호가 일치하는 경우에만 이메일을 바꿀 수 있도록 함
    public void setEmail(String email, String currentPassword) {
        if (this.password.equals(currentPassword)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }


    // 현재 이메일과 비밀번호가 일치하는 경우에만 비밀번호를 바꿀 수 있도록 함
    public void setPassword(String newPassword, String currentPassword) {
        if (this.password.equals(currentPassword)) {
            this.password = newPassword;
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
