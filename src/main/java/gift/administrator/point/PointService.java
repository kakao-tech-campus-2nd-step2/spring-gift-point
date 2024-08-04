package gift.administrator.point;

import gift.users.user.User;
import gift.users.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final UserService userService;

    public PointService(UserService userService) {
        this.userService = userService;
    }

    public void addPoints(Long userId, int points) {
        User user = userService.findById(userId).toUser();
        user.addPoints(points);
        userService.updatePoints(user);
    }

    public int usePoints(Long userId, int points){
        User user = userService.findById(userId).toUser();
        if(points > user.getPoints()){
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        user.usePoints(points);
        userService.updatePoints(user);
        return user.getPoints();
    }
}
