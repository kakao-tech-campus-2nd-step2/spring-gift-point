package gift.DTO;

public class OrderDto {

  private final Long id;
  private final OptionDto optionDto;
  private final int quantity;
  private final String orderDateTime;
  private final String message;

  private final Point point;

  public OrderDto(Long id, OptionDto optionDto, int quantity, String orderDateTime,
    String message, Point point) {
    this.id = id;
    this.optionDto = optionDto;
    this.quantity = quantity;
    this.orderDateTime = orderDateTime;
    this.message = message;
    this.point = point;
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

  public Point getPointVo() {
    return this.point;
  }
}
