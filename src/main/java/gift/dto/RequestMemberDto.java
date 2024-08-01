package gift.dto;

import gift.domain.Role;

public class RequestMemberDto {

    private final String email;
    private final String password;
    private final Role role;

    public RequestMemberDto(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = Role.ADMIN;
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
