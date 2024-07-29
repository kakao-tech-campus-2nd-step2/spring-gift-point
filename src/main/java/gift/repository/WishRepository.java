package gift.repository;

import gift.entity.Product;
import gift.entity.User;
import gift.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WishRepository extends JpaRepository<Wish, Long>, PagingAndSortingRepository<Wish, Long> {
    Page<Wish> findByUser(User user, Pageable pageable);

    void deleteByUserAndProduct(User user, Product product);

}
