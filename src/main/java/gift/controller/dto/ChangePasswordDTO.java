package gift.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class ChangePasswordDTO {

    @Email
    @NotNull
    private String email;
    @NotNull
    private String currentPassword;
    @NotNull
    private String newPassword;

    public ChangePasswordDTO(String email, String currentPassword, String newPassword) {
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
