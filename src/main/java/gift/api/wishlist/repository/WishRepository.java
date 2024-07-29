package gift.api.wishlist.repository;

import gift.api.member.domain.Member;
import gift.api.wishlist.domain.Wish;
import gift.api.wishlist.domain.WishId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, WishId> {
    Page<Wish> findAllByMember(Member member, Pageable pageable);
}
