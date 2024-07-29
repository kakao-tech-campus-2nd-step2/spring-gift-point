package gift.domain;

import gift.controller.member.MemberRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
public class Member {

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private final List<Wish> wishes = new LinkedList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickName;
    @Column
    @Enumerated(EnumType.STRING)
    private Grade grade;

    public Member() {
    }

    public Member(String email, String password, String nickName) {
        this.email = email;
        this.password = password;
        this.nickName = nickName;
    }

    @PrePersist
    public void prePersist() {
        if (grade == null) {
            grade = Grade.USER;
        }
    }

    public String getNickName() {
        return nickName;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setMember(MemberRequest member) {
        this.email = member.email();
        this.password = member.password();
        this.nickName = member.nickName();
        this.grade = member.grade();
    }
}