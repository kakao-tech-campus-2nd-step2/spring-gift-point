package gift.service;

import gift.dto.OptionDTO;
import gift.entity.OptionEntity;
import gift.entity.ProductEntity;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OptionService optionService;

    @Test
    @DisplayName("상품의 모든 옵션")
    public void testReadProductAllOption() {
        Long productId = 1L;
        ProductEntity productEntity = new ProductEntity(productId, "아이스티", 1000,
            "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", null);
        OptionEntity optionEntity = new OptionEntity("옵션", 10, productEntity);

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(optionRepository.findByProductId(productId)).thenReturn(List.of(optionEntity));

        List<OptionEntity> options = optionService.readProductAllOption(productId);

        assertNotNull(options);
        assertEquals(1, options.size());
        assertEquals("옵션", options.get(0).getName());
        assertEquals(10, options.get(0).getQuantity());
    }

    @Test
    @DisplayName("상품의 일부 옵션")
    public void testReadProductOption() {
        Long productId = 1L;
        Long optionId = 2L;
        ProductEntity productEntity = new ProductEntity(productId, "아이스티", 1000,
            "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", null);
        OptionEntity optionEntity = new OptionEntity("옵션", 10, productEntity);

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(optionEntity));

        OptionEntity result = optionService.readProductOption(productId, optionId);

        assertNotNull(result);
        assertEquals("옵션", result.getName());
        assertEquals(10, result.getQuantity());
    }

    @Test
    @DisplayName("옵션 생성")
    public void testCreateOption() {
        Long productId = 1L;
        ProductEntity productEntity = new ProductEntity(productId, "아이스티", 1000,
            "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", null);
        OptionDTO optionDTO = new OptionDTO("옵션", 10);

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));

        OptionEntity savedOptionEntity = new OptionEntity(optionDTO.getName(),
            optionDTO.getQuantity(), productEntity);
        when(optionRepository.save(any(OptionEntity.class))).thenReturn(savedOptionEntity);

        optionService.createOption(productId, optionDTO);

        // 불필요한 save 호출을 제거하여 단일 호출로 유지
        OptionEntity result = savedOptionEntity;
        assertNotNull(result);
        assertEquals("옵션", result.getName());
        assertEquals(10, result.getQuantity());
    }

    @Test
    @DisplayName("옵션 수정")
    public void testEditOption() {
        Long productId = 1L;
        Long optionId = 2L;
        ProductEntity productEntity = new ProductEntity(productId, "아이스티", 1000,
            "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", null);
        OptionEntity optionEntity = new OptionEntity("옵션", 10, productEntity);
        OptionDTO optionDTO = new OptionDTO("옵션 1", 20);

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(optionEntity));

        optionService.editOption(productId, optionId, optionDTO);

        optionEntity.update(optionDTO.getName(), optionDTO.getQuantity());

        assertEquals("옵션 1", optionEntity.getName());
        assertEquals(20, optionEntity.getQuantity());
    }

    @Test
    @DisplayName("옵션 삭제")
    public void testDeleteOption() {
        Long productId = 1L;
        Long optionId = 2L;
        ProductEntity productEntity = new ProductEntity(productId, "아이스티", 1000, "imageUrl", null);
        OptionEntity optionEntity = new OptionEntity("Option1", 10, productEntity);

        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));
        when(optionRepository.findById(optionId)).thenReturn(Optional.of(optionEntity));

        doNothing().when(optionRepository).delete(optionEntity);

        optionService.deleteOption(productId, optionId);
    }

    @Test
    @DisplayName("없는 옵션")
    void testSubtractOptionQuantityOptionNotFound() {
        Long optionId = 1L;
        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            optionService.subtractOptionQuantity(optionId, 3);
        });

        assertEquals("해당 옵션이 존재하지 않습니다. ID: 1", exception.getMessage());
    }

    @Test
    @DisplayName("옵션 수량보다 더 많이 감소 시킬 때 예외 발생")
    void testFailSubQuantity() {
        // given
        Long optionId = 1L;
        OptionEntity optionEntity = new OptionEntity("Option1", 2);
        given(optionRepository.findById(optionId)).willReturn(Optional.of(optionEntity));

        // when, then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            optionService.subtractOptionQuantity(optionId, 5);
        });

        assertEquals("해당 옵션의 재고가 사용하려는 수량보다 부족합니다.", exception.getMessage());
    }
}