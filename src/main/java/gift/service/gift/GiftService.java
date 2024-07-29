package gift.service.gift;


import gift.dto.gift.GiftRequest;
import gift.dto.gift.GiftResponse;
import gift.dto.option.OptionRequest;
import gift.dto.paging.PagingResponse;
import gift.model.category.Category;
import gift.model.gift.Gift;
import gift.model.option.Option;
import gift.repository.category.CategoryRepository;
import gift.repository.gift.GiftRepository;
import gift.repository.option.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class GiftService {

    private final GiftRepository giftRepository;

    private final CategoryRepository categoryRepository;


    @Autowired
    public GiftService(GiftRepository giftRepository, CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.giftRepository = giftRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public PagingResponse<GiftResponse> getAllGifts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<Gift> gifts = giftRepository.findAll(pageRequest);
        List<GiftResponse> giftResponses = gifts.stream()
                .map(GiftResponse::from)
                .collect(Collectors.toList());
        return new PagingResponse<>(page, giftResponses, size, gifts.getTotalElements(), gifts.getTotalPages());
    }

    @Transactional(readOnly = true)
    public GiftResponse getGift(Long id) {
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 상품을 찾을 수 없습니다 id :  " + id));
        return GiftResponse.from(gift);
    }

    @Transactional
    public GiftResponse addGift(GiftRequest.Create giftRequest) {
        Category category = categoryRepository.findById(giftRequest.categoryId())
                .orElseThrow(() -> new NoSuchElementException("해당 카테고리 id가 없습니다."));

        List<Option> options = giftRequest.options().stream().map(OptionRequest.Create::toEntity).toList();

        Gift gift = new Gift(giftRequest.name(), giftRequest.price(), giftRequest.imageUrl(), category, options);
        return GiftResponse.from(giftRepository.save(gift));
    }

    @Transactional
    public void updateGift(GiftRequest.Update giftRequest, Long id) {
        Category category = categoryRepository.findById(giftRequest.categoryId())
                .orElseThrow(() -> new NoSuchElementException("해당 카테고리 id가 없습니다."));
        Gift gift = giftRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 Gift가 없습니다. id : " + id));
        gift.modify(giftRequest.name(), giftRequest.price(), giftRequest.imageUrl(), category);
        giftRepository.save(gift);
    }

    @Transactional
    public void deleteGift(Long id) {
        giftRepository.deleteById(id);
    }
}




