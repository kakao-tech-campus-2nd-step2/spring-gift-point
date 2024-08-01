package gift.dto.point;

import gift.model.user.User;

public class MemberPointResponse {
    public record Info(
            int point
    ){
        public static Info fromEntity(User user){
            return new Info(user.getPoint());
        }

    }
}
