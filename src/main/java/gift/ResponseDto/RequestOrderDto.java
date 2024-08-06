package gift.ResponseDto;

import gift.DTO.Point;

public class RequestOrderDto {

  private final Long optionId;
  private final int quantity;
  private final String message;
  private final Point point;

  public RequestOrderDto(Long optionId, int quantity, String message, Point point) {
    this.optionId = optionId;
    this.quantity = quantity;
    this.message = message;
    this.point = point;
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

  public Point getUsedPointVo() {
    return this.point;
  }
}
