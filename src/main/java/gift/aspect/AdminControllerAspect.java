package gift.aspect;

import gift.user.exception.ForbiddenException;
import gift.user.model.dto.AppUser;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminControllerAspect {

    @Before("within(@gift.aspect.AdminController *) && args(appUser,..)")
    public void verifyAdminAccess(AppUser appUser) {
        if (!appUser.isAdmin()) {
            throw new ForbiddenException("해당 요청에 대한 관리자 권한이 없습니다.");
        }
    }

}
