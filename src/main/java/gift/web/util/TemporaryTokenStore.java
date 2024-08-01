package gift.web.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class TemporaryTokenStore {
    private final ConcurrentHashMap<String, String> tokenStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // 토큰 저장
    public void storeToken(String email, String token) {
        tokenStore.put(email, token);

        // 기존 스케줄된 email의 토큰이 있다면, 스케줄 삭제
        ScheduledFuture<?> existingTask = scheduledTasks.remove(email);
        if(existingTask != null) {
            existingTask.cancel(false);
        }

        ScheduledFuture<?> scheduledTask = scheduleTokenRemove(email, 120L, TimeUnit.MINUTES);
        scheduledTasks.put(email, scheduledTask);
    }

    private ScheduledFuture<?> scheduleTokenRemove(String email, Long delay, TimeUnit timeUnit) {
        return scheduler.schedule(() -> {
            tokenStore.remove(email);
            scheduledTasks.remove(email);
        }, delay, timeUnit);
    }

    public String getAndRemoveToken(String email) {
        String token = tokenStore.remove(email);

        ScheduledFuture<?> scheduledTask = scheduledTasks.remove(email);
        if(scheduledTask != null) {
            scheduledTask.cancel(false);
        }

        return token;
    }
}
