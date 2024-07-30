package gift.domain.order.service;

public interface MessageApiProvider <T> {

    T sendMessageToMe(String accessToken, String templateObject);
}
