package gift.repository;

import gift.model.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByMember_Id(Long memberId, Pageable pageable);

    Optional<Wish> findByMember_IdAndProduct_Id(Long memberId, Long productId);
}
