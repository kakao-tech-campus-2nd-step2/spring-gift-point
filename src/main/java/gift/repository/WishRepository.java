package gift.repository;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByMember(Member member, Pageable pageable);
    Optional<Wish> findById(Long id);
    Wish save(Wish wish);
    void delete(Wish wish);
    Wish findByProduct(Product product);
}
