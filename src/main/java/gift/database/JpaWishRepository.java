package gift.database;

import gift.model.Wish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWishRepository extends JpaRepository<Wish, Long> {

    <S extends Wish> S save(S entity);

    Optional<Wish> findById(Long id);

    Optional<Wish> findByMemberId(Long memberId);

    List<Wish> findAllByMemberId(Long memberId);

    void delete(Wish entity);

    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);

    Page<Wish> findByMemberId(Long memberId, Pageable pageable);

}
