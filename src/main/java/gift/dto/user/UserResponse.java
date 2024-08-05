package gift.dto.user;

import gift.model.user.User;

public class UserResponse {
    public record Info(
            Long id,
            String email,
            String name,
            int point
    ){
        public static Info fromEntity(User user){
            return new Info(user.getId(),user.getEmail(), user.getName(), user.getPoint());
        }
    }
}
