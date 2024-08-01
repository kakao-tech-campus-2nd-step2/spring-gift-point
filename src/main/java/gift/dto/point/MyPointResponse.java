package gift.dto.point;

import gift.model.user.User;

public class MyPointResponse {
    public record Info(
            Integer point
    ){
        public static Info fromEntity(User user){
            return new Info(user.getPoint());
        }
    }
}
