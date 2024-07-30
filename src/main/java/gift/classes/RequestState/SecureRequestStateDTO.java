package gift.classes.RequestState;

public class SecureRequestStateDTO extends RequestStateDTO {

    public String secure;

    public SecureRequestStateDTO(RequestStatus requestStatus, String details, String secure) {
        super(requestStatus, details);
        this.secure = secure;
    }

    public String getSecure() {
        return secure;
    }
}