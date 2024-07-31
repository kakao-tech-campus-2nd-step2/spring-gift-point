package gift.product.exception.kakao;

import org.springframework.http.HttpStatus;

public class KakaoMessageException extends KakaoApiException {
    private static final String MESSEAGE = "카카오톡 메시지 전송에 실패하였습니다. 구매 완료여부를 확인해주세요";
    private static final HttpStatus HTTP_STATUS = HttpStatus.MULTI_STATUS;

    public KakaoMessageException() {
        super(MESSEAGE, HTTP_STATUS);
    }
}
