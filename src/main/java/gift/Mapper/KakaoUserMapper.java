package gift.Mapper;

import org.springframework.stereotype.Component;
import gift.Entity.KakaoUserEntity;
import gift.DTO.KakaoUserDTO;

@Component
public class KakaoUserMapper {

    public KakaoUserDTO toDTO(KakaoUserEntity entity) {
        KakaoUserDTO dto = new KakaoUserDTO();
        dto.setKakaoId(entity.getKakaoId());
        dto.setEmail(entity.getEmail());
        return dto;
    }

    public KakaoUserEntity toEntity(KakaoUserDTO dto) {
        KakaoUserEntity entity = new KakaoUserEntity();
        entity.setKakaoId(dto.getKakaoId());
        entity.setEmail(dto.getEmail());
        return entity;
    }
}
