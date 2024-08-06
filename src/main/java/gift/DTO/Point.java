package gift.DTO;

public class Point {

  private final int point;

  public Point() {
    this.point = 0;
  }

  public Point(int point) {
    this.point = point;
  }

  public int getPoint() {
    return point;
  }

  public Point subtractPoint(Point point) throws IllegalAccessException {
    if (point.getPoint() > this.point) {
      throw new IllegalAccessException("보유 포인트가 차감 포인트보다 적습니다.");
    }
    return new Point(this.point - point.getPoint());
  }

  public Point addPoint(Point point) throws IllegalAccessException {
    if (point.getPoint() < 0) {
      throw new IllegalAccessException("point는 1원 이상 충전 가능합니다");
    }
    return new Point(this.point + point.getPoint());
  }
}
