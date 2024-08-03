package gift.dto;

import java.time.LocalDateTime;

public class OrderDto {
  private Long id;
  private Long optionId;
  private int quantity;
  private LocalDateTime orderDateTime;
  private String message;
  private int pointsUsed;
  private int pointsEarned;
  private int memberPoints;
  private Long memberId;
  private boolean usePoint;
  private int totalPrice;
  private int pointUsed;
  private int remainPoint;

  public OrderDto(Long id, Long optionId, int quantity, LocalDateTime orderDateTime, String message, int pointsUsed, int pointsEarned, int memberPoints, Long memberId, boolean usePoint, int totalPrice, int pointUsed, int remainPoint) {
    this.id = id;
    this.optionId = optionId;
    this.quantity = quantity;
    this.orderDateTime = orderDateTime;
    this.message = message;
    this.pointsUsed = pointsUsed;
    this.pointsEarned = pointsEarned;
    this.memberPoints = memberPoints;
    this.memberId = memberId;
    this.usePoint = usePoint;
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

  public int getQuantity() {
    return quantity;
  }

  public LocalDateTime getOrderDateTime() {
    return orderDateTime;
  }

  public Long getMemberId() {
    return memberId;
  }

  public boolean isUsePoint() {
    return usePoint;
  }

  public String getMessage() {
    return message;
  }

  public int getPointsUsed() {
    return pointsUsed;
  }

  public int getPointsEarned() {
    return pointsEarned;
  }

  public int getMemberPoints() {
    return memberPoints;
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