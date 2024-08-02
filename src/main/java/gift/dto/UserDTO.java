package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private int point; // 포인트 필드 추가

    public UserDTO() {}

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
        this.point = 0; // 포인트 기본값 설정
    }

    public UserDTO(String email, String password, int point) {
        this.email = email;
        this.password = password;
        this.point = point;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}