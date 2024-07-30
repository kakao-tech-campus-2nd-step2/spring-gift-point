package gift.util;

public class CommonResponse<T> {

    private T data;
    private String message;
    private Boolean result;

    public CommonResponse(T data, String message, Boolean result) {
        this.data = data;
        this.message = message;
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return result;
    }

    public void setStatus(Boolean result) {
        this.result = result;
    }
}
