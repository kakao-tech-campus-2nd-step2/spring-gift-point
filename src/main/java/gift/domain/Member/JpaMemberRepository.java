package gift.domain.Member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMemberRepository extends JpaRepository<Member, Long> {


    @Modifying
    @Query(value = "DELETE FROM cart_item WHERE user_id = :memberId; " +
                   "DELETE FROM members WHERE id = :memeberId", nativeQuery = true)
    void deleteById(Long memberId);

    boolean existsByEmail(String email);

    Optional<Member> findByEmailAndPassword(String email, String password);

    Optional<Member> findById(Long memberId);

    Member findByEmail(String email);
}
