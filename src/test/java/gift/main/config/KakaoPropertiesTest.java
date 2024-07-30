package gift.main.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KakaoPropertiesTest {

    private final KakaoProperties kakaoProperties;

    @Autowired
    KakaoPropertiesTest(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    @Test
    void getKakaoProperties() {
        assertThat(kakaoProperties.password()).isEqualTo("seohyun1228");
    }

}