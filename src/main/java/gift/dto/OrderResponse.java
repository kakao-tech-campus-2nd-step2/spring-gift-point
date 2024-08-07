package gift.dto;

import java.time.LocalDateTime;

public class OrderResponse {
  private final Long id;
  private final Long optionId;
  private final Integer quantity;
  private final LocalDateTime orderDateTime;
  private final String message;
  private final int totalPrice;
  private final int pointUsed;
  private final int remainPoint;

  public OrderResponse(Long id, Long optionId, int quantity, LocalDateTime orderDateTime, String message, int totalPrice, int pointUsed, int remainPoint) {
    this.id = id;
    this.optionId = optionId;
    this.quantity = quantity;
    this.orderDateTime = orderDateTime;
    this.message = message;
    this.totalPrice = totalPrice;
    this.pointUsed = pointUsed;
    this.remainPoint = remainPoint;
  }
  public Long getId() {
    return id;
  }

  public Long getOptionId() {
    return optionId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public LocalDateTime getOrderDateTime() {
    return orderDateTime;
  }

  public String getMessage() {
    return message;
  }

  public int getTotalPrice() {
    return totalPrice;
  }

  public int getPointUsed() {
    return pointUsed;
  }

  public int getRemainPoint() {
    return remainPoint;
  }
}