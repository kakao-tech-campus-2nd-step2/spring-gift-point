package gift.Service;

import gift.DTO.WishDTO;
import gift.Entity.WishEntity;
import gift.Mapper.WishServiceMapper;
import gift.Repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishService {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private WishServiceMapper wishServiceMapper;

    public List<WishDTO> findAllWishes() {
        List<WishEntity> wishEntities = wishRepository.findAll();
        return wishServiceMapper.convertToWishDTOs(wishEntities);
    }

    public Optional<WishDTO> findWishById(Long id) {
        Optional<WishEntity> wishEntity = wishRepository.findById(id);
        return wishEntity.map(wishServiceMapper::convertToDTO);
    }

    public WishDTO saveWish(WishDTO wishDTO) {
        WishEntity wishEntity = wishServiceMapper.convertToEntity(wishDTO);
        WishEntity savedWishEntity = wishRepository.save(wishEntity);
        return wishServiceMapper.convertToDTO(savedWishEntity);
    }

    public WishDTO updateWish(Long id, WishDTO wishDTO) {
        Optional<WishEntity> existingWish = wishRepository.findById(id);
        if (existingWish.isPresent()) {
            WishEntity wish = existingWish.get();
            wish.setProduct(wishServiceMapper.convertToProductEntity(wishDTO.getProductId()));
            wish.setUser(wishServiceMapper.convertToUserEntity(wishDTO.getUserId()));
            WishEntity updatedWishEntity = wishRepository.save(wish);
            return wishServiceMapper.convertToDTO(updatedWishEntity);
        } else {
            throw new RuntimeException("Wish not found");
        }
    }

    public void deleteWish(Long id) {
        wishRepository.deleteById(id);
    }
}
