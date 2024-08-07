package gift.repository;

import gift.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findById(int id);
    int searchIdByToken(String token);
    Boolean existsByToken(String token);
    Member save(Member member);
    @Modifying
    @Query("UPDATE Member m SET m.point = m.point + :newPoint WHERE m.token = :token")
    int updatePointByToken(@Param("token") int token, @Param("newPoint") String newPoint);
}