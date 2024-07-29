package gift.repository;

import gift.entity.KakaoUser;
import gift.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoUserRepository extends JpaRepository<KakaoUser, Integer> {
    @Query("select k.user from KakaoUser k where k.kakaoUserId=:kakaoUserId")
    Optional<User> findByKakaoUserId(Long kakaoUserId);
}
