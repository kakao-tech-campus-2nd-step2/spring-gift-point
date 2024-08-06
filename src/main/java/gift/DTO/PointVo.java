package gift.DTO;

public class PointVo {

  private final int point;

  public PointVo() {
    this.point = 0;
  }

  public PointVo(int point) {
    this.point = point;
  }

  public int getPoint() {
    return point;
  }

  public PointVo subtractPoint(PointVo pointVo) throws IllegalAccessException {
    if (pointVo.getPoint() > this.point) {
      throw new IllegalAccessException("보유 포인트가 차감 포인트보다 적습니다.");
    }
    return new PointVo(this.point - pointVo.getPoint());
  }

  public PointVo addPoint(PointVo pointVo) throws IllegalAccessException {
    if (pointVo.getPoint() < 0) {
      throw new IllegalAccessException("point는 1원 이상 충전 가능합니다");
    }
    return new PointVo(this.point + pointVo.getPoint());
  }
}
