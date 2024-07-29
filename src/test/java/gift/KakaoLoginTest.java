/*
package gift;

import gift.exception.KakaoException;
import gift.kakaoLogin.KakaoLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class KakaoLoginTest {

    @Autowired
    private KakaoLoginService kakaoLoginService;


    @Test
    public void LoginSuccess() {

        assertThat(kakaoLoginService.login("Legal code")).isInstanceOf(String.class);
    }

    @Test
    public void LoginFail() {

        assertThrows(KakaoException.class, () -> kakaoLoginService.login("Illegal code"));
    }
}
*/
