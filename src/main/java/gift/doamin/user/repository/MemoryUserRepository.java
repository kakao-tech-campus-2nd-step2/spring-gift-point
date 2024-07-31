package gift.doamin.user.repository;

import gift.doamin.user.entity.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public void save(User user) {
        Long id = sequence.getAndIncrement();
        user.setId(id);
        users.put(id, user);
    }

    @Override
    public User findById(Long id) {
        return users.get(id);
    }

    @Override
    public User findByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}
