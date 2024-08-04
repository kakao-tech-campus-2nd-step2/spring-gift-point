package gift.init.auth;

import gift.domain.auth.User.CreateUser;
import gift.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserCreator {

    private final UserService userService;

    @Autowired
    public UserCreator(UserService userService) {
        this.userService = userService;
    }

    public void creator() {
        userService.createUser(new CreateUser("kakao1@kakao.com", "1234"));
        userService.createUser(new CreateUser("kakao2@kakao.com", "123456"));
        userService.createUser(new CreateUser("kakao3@kakao.com", "1234"));
        userService.createUser(new CreateUser("kakao4@kakao.com", "qwer1234"));
        userService.createUser(new CreateUser("kakao5@kakao.com", "qer1234"));
    }
}
