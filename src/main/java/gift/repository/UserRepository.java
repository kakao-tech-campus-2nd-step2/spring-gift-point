package gift.repository;

import gift.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    Page<User> findAll(Pageable pageable);

    Optional<User> findById(Long id);

    User findByUserId(String userId);

    void deleteById(Long id);

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    boolean existsById(Long id);

    boolean existsByUserIdAndPassword(String userId, String password);
}
