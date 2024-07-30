package gift.exception;

public class OptionNotFoundException extends RuntimeException {
    public OptionNotFoundException(Long optionId) {
        super("OptionId: " + optionId + " not found.");
    }
}
