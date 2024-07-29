package gift.repository;

import gift.model.Option;
import gift.model.User;
import gift.model.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishRepository extends JpaRepository<Wish, Long> {
    @Query("SELECT w FROM Wish w JOIN FETCH w.option WHERE w.user = :user")
    Page<Wish> findAllByUser(@Param("user") User user, Pageable pageable);

    void deleteByUserAndOption(User user, Option option);
}
