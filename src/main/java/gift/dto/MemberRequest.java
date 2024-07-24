package gift.dto;

import gift.model.MemberRole;
import jakarta.validation.constraints.NotBlank;

public class MemberRequest {

    private Long id;
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;
    private MemberRole role;

    public MemberRequest() {
    }

    public MemberRequest(String email, String password, MemberRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
        if (this.role == null) {
            this.role = MemberRole.COMMON_MEMBER;
        }
    }

    public MemberRequest(Long id, String email, String password, MemberRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MemberRole getRole() {
        return role;
    }

    public void setRole(MemberRole role) {
        this.role = role;
    }
}
