package gift.vo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_member", columnNames = {"email"})})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    public Member(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member() {
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

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 비즈니스 로직을 통한 Member 정보 업데이트
     * @param newEmail 업데이트할 이메일
     * @param newPassword 업데이트할 비밀번호
     */
    public void updateMemberInfo(String newEmail, String newPassword) {
        if (!this.email.equals(newEmail)) {
            setEmail(newEmail);
        }

        if (newPassword != null && !newPassword.isEmpty() &&!this.password.equals(newPassword)) {
            this.password = newPassword;
        }
    }

    /**
     * 객체와 매개변수로 받은 email이 일치하는지 검증하는 메소드
     * @param email 검증할 email
     */
    public void validateEmail(String email) {
        if (!this.email.equals(email)) {
            throw new RuntimeException("입력하신 이메일로 가입된 회원이 존재하지 않습니다.");
        }
    }
}
