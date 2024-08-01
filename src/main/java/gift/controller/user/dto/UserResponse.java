package gift.controller.user.dto;

public class UserResponse {

    public record Login(String name) {

        public static Login from(String name) {
            return new Login(name);
        }
    }

    public record Point(int point) {

        public static Point from(int point) {
            return new Point(point);
        }
    }

}