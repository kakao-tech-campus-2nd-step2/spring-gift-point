package gift.exception;

public class ErrorCode {
    public static final String INVALID_NAME_LENGTH = "상품 이름은 공백 포함 20글자를 넘을 수 없습니다.";
    public static final String INVALID_CHARACTERS = "상품 이름에는 특수문자 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.";
    public static final String CONTAINS_KAKAO = "상품 이름에 '카카오'가 포함되면 담당 MD와 협의가 필요합니다.";

    // 생성자는 private로 생성함으로써 객체생성 방지
    private ErrorCode() {
    }

}
