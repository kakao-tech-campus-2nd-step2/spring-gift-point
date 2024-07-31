package gift.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class LocalMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(unique = true)
    private Member member;

    protected LocalMember() {
    }

    public LocalMember(String password, Member member) {
        this.password = password;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return "LocalMember{" +
            "id=" + id +
            ", password='" + password + '\'' +
            ", member=" + member +
            '}';
    }
}
