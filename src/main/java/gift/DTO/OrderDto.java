package gift.DTO;

import java.util.List;

public class OrderDto {

  private final Long id;
  private final OptionDto optionDto;
  private final int quantity;
  private final String orderDateTime;
  private final String message;

  public OrderDto(Long id, OptionDto optionDto, int quantity, String orderDateTime,
    String message) {
    this.id = id;
    this.optionDto = optionDto;
    this.quantity = quantity;
    this.orderDateTime = orderDateTime;
    this.message = message;
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
}
