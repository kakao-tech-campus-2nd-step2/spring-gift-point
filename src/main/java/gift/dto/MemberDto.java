package gift.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import gift.domain.Role;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDto {

    private final Long id;
    private final String email;
    private String password;
    private Role role;


    public MemberDto(Long id, String email, String password, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = Role.ADMIN;
    }

    public MemberDto(Long id, String email) {
        this.id = id;
        this.email = email;
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

    public Role getRole() {
        return role;
    }
}
