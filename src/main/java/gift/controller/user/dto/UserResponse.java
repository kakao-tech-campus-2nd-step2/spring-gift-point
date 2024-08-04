package gift.controller.user.dto;

import gift.common.enums.Role;
import gift.model.User;
import java.util.List;

public class UserResponse {

    public record Login(String name, Role role) {

        public static Login from(String name, Role role) {
            return new Login(name, role);
        }
    }

    public record Point(int point) {

        public static Point from(int point) {
            return new Point(point);
        }
    }

    public record Info(
        Long id,
        String email,
        String name,
        int point
    ) {

        public static Info from(User user) {
            return new Info(user.getId(), user.getEmail(), user.getName(), user.getPoint());
        }
    }
}