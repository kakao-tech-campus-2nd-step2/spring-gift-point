package gift.domain.order.repository;


import gift.domain.member.entity.Member;
import gift.domain.order.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findAllByMember(Pageable pageable, Member member);
}
