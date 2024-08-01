package gift.service.kakaoAuth;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KakaoPropertiesTest {

    @Autowired
    private KakaoProperties kakaoProperties;

    @Test
    void testkakaoProperties() {
        assertNotNull(kakaoProperties.clientId());
        assertNotNull(kakaoProperties.redirectUri());
        System.out.println(kakaoProperties.clientId());
        System.out.println(kakaoProperties.redirectUri());
    }

    @Test
    void methodTest() {
        var body = kakaoProperties.createBody("1234");
        Assertions.assertThat(body).isNotNull();
        System.out.println(body);
    }

}