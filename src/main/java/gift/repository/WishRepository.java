package gift.repository;

import gift.entity.MemberEntity;
import gift.entity.WishEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, Long> {

    List<WishEntity> findByMemberEntity(MemberEntity memberEntity);

    Page<WishEntity> findByMemberEntity(MemberEntity memberEntity, Pageable pageable);

}
