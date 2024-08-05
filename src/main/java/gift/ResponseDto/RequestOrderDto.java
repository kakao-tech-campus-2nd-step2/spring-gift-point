package gift.ResponseDto;

import gift.DTO.PointVo;

public class RequestOrderDto {

  private final Long optionId;
  private final int quantity;
  private final String message;
  private final PointVo pointVo;

  public RequestOrderDto(Long optionId, int quantity, String message, PointVo pointVo) {
    this.optionId = optionId;
    this.quantity = quantity;
    this.message = message;
    this.pointVo = pointVo;
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

  public PointVo getUsedPointVo() {
    return this.pointVo;
  }
}
