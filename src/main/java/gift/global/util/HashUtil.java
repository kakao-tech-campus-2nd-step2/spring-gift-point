package gift.global.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 주어진 문자열에 대한 256비트 해시코드를 생성하는 유틸 클래스
 */
public class HashUtil {

    public static String hashCode(String target) {
        return DigestUtils.sha256Hex(target);
    }
}
