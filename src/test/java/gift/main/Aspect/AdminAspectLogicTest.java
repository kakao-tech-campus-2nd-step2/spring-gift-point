package gift.main.Aspect;

import gift.main.dto.UserVo;
import gift.main.entity.Role;
import org.junit.jupiter.api.Test;

public final class AdminAspectLogicTest {
    /*
    System.out.println("실행된다.");
        if (!Role.ADMIN.equals(userVo.getRole())) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }
     */
    @Test
    public void testEnum() {
        UserVo user = new UserVo(1L, "test", "email", "user");
        UserVo admin = new UserVo(1L, "test", "email", "admin");
        System.out.println("Role.ADMIN.equals(user.getRole()) = " + Role.ADMIN.equals(user.getRole()));
        System.out.println("Role.ADMIN.equals(admin.getRole()) = " + Role.ADMIN.equals(admin.getRole()));
        /*
        <실행결과>
        Role.ADMIN.equals(user.getRole()) = false
        Role.ADMIN.equals(admin.getRole()) = false
        - 멘토님 false를 말하고 싶으셨던 것 맞나요?!
         */


        System.out.println("Role.ADMIN.getRole().equals(user.getRole()) = " + Role.ADMIN.getRole().equals(user.getRole()));
        System.out.println("Role.ADMIN.getRole().equals(admin.getRole()) = " + Role.ADMIN.getRole().equals(admin.getRole()));

        /*
        <수정한 실행결과>
        Role.ADMIN.getRole().equals(user.getRole()) = false
        Role.ADMIN.getRole().equals(admin.getRole()) = true
         */
    }
}
