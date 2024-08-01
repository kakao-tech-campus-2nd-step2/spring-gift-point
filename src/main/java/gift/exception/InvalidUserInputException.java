package gift.exception;

public class InvalidUserInputException extends BadRequestException {
  public InvalidUserInputException(String message) {
    super(message);
  }
}
