package gift.ResponseDto;

public class RequestOrderDto {
  private final Long optionId;
  private final int quantity;
  private final String message;

  public RequestOrderDto(Long optionId, int quantity, String message) {
    this.optionId = optionId;
    this.quantity = quantity;
    this.message = message;
  }

  public Long getOptionId() {
    return optionId;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getMessage() {
    return message;
  }
}
