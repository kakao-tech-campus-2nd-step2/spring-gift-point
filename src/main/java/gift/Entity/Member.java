package gift.Entity;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;
    private String email;
    private String name;
    private String password;
    private boolean isAdmin;
    private long kakaoId;

    protected Member() {
    }

    public Member(long memberId, String email, String name, String password, boolean isAdmin) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // Getters and setters
    public long getId() {
        return memberId;
    }

    public void setId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setKakaoId(long kakaoId) {
        this.kakaoId = kakaoId;
    }
}