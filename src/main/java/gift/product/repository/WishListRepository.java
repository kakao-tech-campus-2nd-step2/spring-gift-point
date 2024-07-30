package gift.product.repository;

import gift.product.dto.WishResponseDTO;
import gift.product.model.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface WishListRepository extends JpaRepository<Wish, Long> {
    Page<WishResponseDTO> findAllByMemberId(Long memberId, Pageable pageable);
}
