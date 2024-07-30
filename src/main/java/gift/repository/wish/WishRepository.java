package gift.repository.wish;

import gift.domain.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface WishRepository extends JpaRepository<Wish, Long> {

    @Query("SELECT w FROM Wish w WHERE w.member.email = :email")
    List<Wish> findWishByByMemberEmail(@Param("email") String email);

    @Query("SELECT w FROM Wish w " +
            "WHERE w.id = :id and w.member.email = :email")
    Optional<Wish> findWishByIdAndMemberEmail(@Param("id") Long id, @Param("email") String email);

    Page<Wish> findWishesByMemberEmail(String email, Pageable pageable);

    Optional<Wish> findWishByProductIdAndMemberEmail(Long productId, String email);

}
