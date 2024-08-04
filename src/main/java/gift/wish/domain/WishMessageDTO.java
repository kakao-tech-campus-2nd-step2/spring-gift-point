package gift.wish.domain;

public class WishMessageDTO {
    private String message;

    public WishMessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
