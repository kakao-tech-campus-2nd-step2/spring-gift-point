package gift.model.dto;

import gift.model.entity.User;
import gift.model.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    @NotNull
    private final Long id;
    @NotBlank
    private final String email;
    @NotBlank
    private final String password;
    private final Role role;

    public UserDTO(Long id, String email, String password, Role role) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}