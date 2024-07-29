package gift.dto;

public class MemberResponseDto {
    private String message;

    public MemberResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
