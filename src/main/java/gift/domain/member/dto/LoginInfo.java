package gift.domain.member.dto;


/**
 * 토큰에서 추출한 사용자 정보를 담음
 */
public class LoginInfo {

    private Long id;
    private String email;

    public LoginInfo(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public LoginInfo() {
    }

    public String getEmail() {
        return email;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
               "email='" + email + '\'' +
               ", id=" + id +
               '}';
    }
}
