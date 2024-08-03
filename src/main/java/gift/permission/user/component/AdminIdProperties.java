package gift.permission.user.component;

import gift.permission.util.PlatformCodeUtil;
import java.util.NoSuchElementException;
import org.springframework.boot.context.properties.ConfigurationProperties;

// 플랫폼 별 어드민 id들을 저장
@ConfigurationProperties(prefix = "auth.admin")
public record AdminIdProperties(
    String kakaoAdminId
) {

    // 정해진 플랫폼 코드를 넣으면 해당하는 플랫폼의 어드민 id를 가져옴.
    public String getAdminId(String platformCode) {
        if (platformCode.equals(PlatformCodeUtil.KAKAO_CODE)) {
            return kakaoAdminId;
        }

        throw new NoSuchElementException("존재하는 플랫폼이 아닙니다.");
    }
}
