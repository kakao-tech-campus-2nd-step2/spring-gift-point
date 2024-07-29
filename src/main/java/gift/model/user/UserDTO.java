package gift.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    @NotNull
    private final Long id;
    @NotBlank
    private final String email;
    @NotBlank
    private final String password;

    public UserDTO(Long id, String email, String password) {
        this.id = id;
        this.password = password;
        this.email = email;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
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

    public User toEntity() {
        return new User(id, password, email);
    }

}