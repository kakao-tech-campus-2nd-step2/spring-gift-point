package gift.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    private long id;
    @NotNull
    private String password;
    @NotNull
    private String email;

    public UserDTO(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

}
