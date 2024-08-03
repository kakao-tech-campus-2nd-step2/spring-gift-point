package gift.ResponseDto;

public class RequestOrderDto {

  private final Long optionId;
  private final int quantity;
  private final String message;
  private final int usedPoint;

  public RequestOrderDto(Long optionId, int quantity, String message, int usedPoint) {
    this.optionId = optionId;
    this.quantity = quantity;
    this.message = message;
    this.usedPoint = usedPoint;
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

  public int getUsedPoint() {
    return usedPoint;
  }
}
