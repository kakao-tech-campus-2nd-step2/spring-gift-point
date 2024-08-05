package gift.order.service;

public interface PointOperationSupport {

    void addPoint(Long userId, Long point);

    void subtractPoint(Long userId, Long point);

}
