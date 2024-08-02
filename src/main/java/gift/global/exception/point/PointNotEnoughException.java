package gift.global.exception.point;

public class PointNotEnoughException extends RuntimeException{

    public PointNotEnoughException(int usePoint, int currentPoint) {
        super("사용할 포인트 (" + usePoint + ") 가 보유 포인트 (" + currentPoint + ") 보다 많습니다.");
    }
}
