package gift.repository;

import gift.entity.MemberEntity;
import gift.entity.WishListEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishListEntity, Long> {

    List<WishListEntity> findByMemberEntity(MemberEntity memberEntity);

    Page<WishListEntity> findByMemberEntity(MemberEntity memberEntity, Pageable pageable);

}
