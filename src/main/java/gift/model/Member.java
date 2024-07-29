package gift.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", columnDefinition = "varchar(255) not null unique")
    private String email;

    @Column(name = "PASSWORD", columnDefinition = "varchar(255) not null")
    private String password;

    public Member(Long id, String email, String password){
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Member(String email, String password){
        this.email = email;
        this.password = password;
    }

    protected Member() {
    }

    public boolean comfirmPassword(String password) {
        if(this.password.equals(password)){
            return true;
        }
        throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(email, member.email) && Objects.equals(password, member.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
