package gift.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gift.Repository.KakaoUserRepository;
import gift.Entity.KakaoUserEntity;
import gift.DTO.KakaoUserDTO;
import gift.Mapper.KakaoUserMapper;

@Service
public class KakaoUserService {

    @Autowired
    private KakaoUserRepository kakaoUserRepository;

    @Autowired
    private KakaoUserMapper kakaoUserMapper;

    public KakaoUserDTO saveOrUpdateUser(KakaoUserDTO kakaoUserDTO, String accessToken, String refreshToken) {
        KakaoUserEntity userEntity = kakaoUserRepository.findByKakaoId(kakaoUserDTO.getKakaoId());
        if (userEntity == null) {
            userEntity = kakaoUserMapper.toEntity(kakaoUserDTO);
        }
        userEntity.setEmail(kakaoUserDTO.getEmail());
        userEntity.setAccessToken(accessToken);
        userEntity.setRefreshToken(refreshToken);
        KakaoUserEntity savedUser = kakaoUserRepository.save(userEntity);
        return kakaoUserMapper.toDTO(savedUser);
    }

    public KakaoUserEntity findByKakaoId(String kakaoId) {
        return kakaoUserRepository.findByKakaoId(kakaoId);
    }

    public KakaoUserEntity findByAccessToken(String accessToken) {
        return kakaoUserRepository.findByAccessToken(accessToken);
    }
}
