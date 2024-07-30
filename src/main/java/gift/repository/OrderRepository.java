package gift.repository;

import gift.entity.Member;
import gift.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    public Page<Orders> findByMember(Pageable pageable, Member member);
}
