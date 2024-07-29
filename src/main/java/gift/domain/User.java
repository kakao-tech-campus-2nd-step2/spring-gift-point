package gift.domain;

import gift.util.page.PageParam;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class User {

    public static class getList extends PageParam {

    }

    public static class CreateUser {

        @NotNull(message = "email은 필수 입력 입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "이메일 형식에 맞지 않습니다.")
        private String email;
        @Size(min = 4, max = 30, message = "비밀번호는 4자 이상 30자 미만 입니다.")
        @NotNull
        private String password;

        public CreateUser(String email, String password) {
            this.email = email;
            this.password = password;
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
    }

    public static class UpdateUser {

        @Size(min = 4, max = 30, message = "비밀번호는 4자 이상 30자 미만 입니다.")
        @NotNull
        private String password;

        public UpdateUser() {
        }

        public UpdateUser(String password) {
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class UserSimple {

        private String email;
        private String password;

        public UserSimple(String email, String password) {
            this.email = email;
            this.password = password;
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
    }
}
