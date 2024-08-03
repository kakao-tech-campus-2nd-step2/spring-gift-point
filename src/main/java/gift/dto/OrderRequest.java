package gift.dto;

public class OrderRequest {
  private final long optionId;
  private final int quantity;
  private final String message;
  private final boolean usePoint;

  public OrderRequest(long optionId, int quantity, String message, boolean usePoint) {
    this.optionId = optionId;
    this.quantity = quantity;
    this.message = message;
    this.usePoint = usePoint;
  }

  public long getOptionId() {
    return optionId;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getMessage() {
    return message;
  }

  public boolean isUsePoint() {
    return usePoint;
  }
}