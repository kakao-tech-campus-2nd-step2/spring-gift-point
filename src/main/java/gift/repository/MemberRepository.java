package gift.repository;

import gift.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 회원 리포지토리 인터페이스
 */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    /**
     * 사용자 존재 여부 확인 메서드
     *
     * @param email    이메일
     * @param password 비밀번호
     * @return 사용자 존재 여부
     */
    boolean existsByEmailAndPasswordAndDeleteFalse(String email, String password);

    /**
     * 사용자 ID 조회 메서드
     *
     * @param email    이메일
     * @param password 비밀번호
     * @return 사용자 엔터티
     */
    MemberEntity findByEmailAndPasswordAndDeleteFalse(String email, String password);
}
