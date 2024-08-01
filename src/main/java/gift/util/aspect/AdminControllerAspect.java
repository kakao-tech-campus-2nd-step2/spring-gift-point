package gift.util.aspect;

import gift.domain.AppUser;
import gift.exception.user.ForbiddenException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AdminControllerAspect {

    @Before("within(@gift.util.aspect.AdminController *) && args(appUser,..)")
    public void verifyAdminAccess(AppUser appUser) {
        if (!appUser.isAdmin()) {
            throw new ForbiddenException("해당 요청에 대한 관리자 권한이 없습니다.");
        }
    }

}
