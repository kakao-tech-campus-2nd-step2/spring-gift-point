package gift.repository;

import gift.entity.Member;
import gift.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findByMember(Member member);
    List<Wish> findByProduct_Id(Long productId);
    void deleteByProduct_Id(Long productId);
}
