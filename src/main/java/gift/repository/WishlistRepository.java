package gift.repository;

import gift.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    @Query("SELECT w FROM Wishlist w where w.id BETWEEN :startId AND :endId and w.member.id = :memberId")
    List<Wishlist> findByIdAndIdAndMember_id(@Param("startId") int id1, @Param("endId") int id2, @Param("memberId") int member_id);
    int searchCount_productByMember_idAndProduct_id(int member_id, int product_id);
    void deleteByMember_idAndProduct_id(int member_id, int product_id);
    Optional<Wishlist> findById(int id);
    Wishlist save(Wishlist wishlist);
}