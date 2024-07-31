package gift.doamin.user.repository;

import gift.doamin.user.entity.User;

public interface UserRepository {

    void save(User user);

    User findById(Long id);

    User findByEmail(String email);
}
