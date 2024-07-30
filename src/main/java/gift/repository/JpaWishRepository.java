package gift.repository;

import gift.domain.Wish;
import java.util.List;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWishRepository extends JpaRepository<Wish, Long> {
    @Query("SELECT w FROM Wish w JOIN FETCH w.product p WHERE w.user.id = :userId")
    List<Wish> findAllByUserId(Long userId);

    @Query("SELECT w FROM Wish w JOIN FETCH w.product p WHERE w.user.id = :userId")
    Page<Wish> findAllByUser(@Param("userId") Long userId, Pageable pageable);
}
