package gift.repository;

import gift.domain.Wish;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, UUID> {

    Optional<Wish> findByMemberIdAndProductId(UUID memberId, UUID productId);

    void deleteByMemberIdAndProductId(UUID memberId, UUID productId);

    @NonNull
    Page<Wish> findAll(@NonNull Pageable pageable);

    Page<Wish> findAllByMemberId(UUID memberId, Pageable pageable);
}