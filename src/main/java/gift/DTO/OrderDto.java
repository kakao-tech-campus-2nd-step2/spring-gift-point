package gift.DTO;

public class OrderDto {

  private final Long id;
  private final OptionDto optionDto;
  private final int quantity;
  private final String orderDateTime;
  private final String message;

  private final PointVo pointVo;

  public OrderDto(Long id, OptionDto optionDto, int quantity, String orderDateTime,
    String message, PointVo pointVo) {
    this.id = id;
    this.optionDto = optionDto;
    this.quantity = quantity;
    this.orderDateTime = orderDateTime;
    this.message = message;
    this.pointVo = pointVo;
  }

  public Long getId() {
    return id;
  }

  public OptionDto getOptionDto() {
    return this.optionDto;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getOrderDateTime() {
    return orderDateTime;
  }

  public String getMessage() {
    return message;
  }

  public PointVo getPointVo() {
    return this.pointVo;
  }
}
