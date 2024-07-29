package gift.order.service;

public interface OrderAlarmGateway {

    void sendAlarm(String accessToken, String message);

}
