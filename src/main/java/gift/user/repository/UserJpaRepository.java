package gift.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.user.domain.User;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}