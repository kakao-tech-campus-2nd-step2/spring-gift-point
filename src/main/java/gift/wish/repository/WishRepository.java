package gift.wish.repository;

import gift.user.entity.User;
import gift.wish.entity.Wish;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    Page<Wish> findByUserId(Long userId, Pageable pageable);

    List<Wish> findByUser(User user);
}
