package gift.Event.EventListener;

import gift.Event.EventObject.SendMessageToMeEvent;
import gift.Util.KakaoProperties;
import gift.Util.KakaoUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class KakaoApiEventListener {
    private KakaoProperties properties;
    private KakaoUtil kakaoUtil;

    public KakaoApiEventListener(KakaoProperties properties, KakaoUtil kakaoUtil) {
        this.properties = properties;
        this.kakaoUtil = kakaoUtil;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSendMessageToMeEvent(SendMessageToMeEvent event){
        kakaoUtil.sendMessageToMe(event.getAccessToken(), event.getMessage());
    }
}
