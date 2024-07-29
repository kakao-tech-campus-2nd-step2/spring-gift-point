package gift.exception;

public class KakaoErrorResponse {

    private String error;
    private String errorDescription;
    private String errorCode;

    public KakaoErrorResponse(String error, String errorDescription, String errorCode) {
        this.error = error;
        this.errorDescription = errorDescription;
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
