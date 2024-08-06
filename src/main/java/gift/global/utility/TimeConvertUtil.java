package gift.global.utility;

// 시간 변환 관련 메서드들을 모은 util 클래스
public final class TimeConvertUtil {

    private TimeConvertUtil() {

    }

    public static long minuteToMillis(int minute) {
        return minute * 60L * 1000;
    }

    public static long secondToMillis(int second) {
        return second * 1000L;
    }
}
