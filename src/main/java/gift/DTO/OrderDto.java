package gift.DTO;

import java.util.List;

public class OrderDto {

  private final Long id;
  private final OptionDto optionDto;
  private final int quantity;
  private final String orderDateTime;
  private final String message;

  private final int usedPoint;

  public OrderDto(Long id, OptionDto optionDto, int quantity, String orderDateTime,
    String message,int usedPoint) {
    this.id = id;
    this.optionDto = optionDto;
    this.quantity = quantity;
    this.orderDateTime = orderDateTime;
    this.message = message;
    this.usedPoint=usedPoint;
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

  public int getUsedPoint() {
    return usedPoint;
  }
}
