package gift.classes.RequestState;

public class RequestStateDTO {

    public RequestStatus status;
    public String details;

    public RequestStateDTO(RequestStatus requestStatus, String details) {
        this.status = requestStatus;
        this.details = details;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
}
