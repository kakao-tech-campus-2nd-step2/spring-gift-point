package gift.core.domain.user;

import java.util.Optional;

public interface UserRepository {

    void save(User user);

    boolean existsById(Long id);

    Optional<User> findById(Long id);

    void deleteById(Long id);

}
