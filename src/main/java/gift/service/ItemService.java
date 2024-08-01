package gift.service;

import gift.exception.ErrorCode;
import gift.exception.customException.CustomArgumentNotValidException;
import gift.exception.customException.CustomDuplicateException;
import gift.exception.customException.CustomNotFoundException;
import gift.model.dto.ItemDTO;
import gift.model.dto.OptionDTO;
import gift.model.entity.Category;
import gift.model.entity.Item;
import gift.model.entity.Option;
import gift.model.response.ItemResponse;
import gift.model.response.WishListResponse;
import gift.repository.CategoryRepository;
import gift.repository.ItemRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public ItemService(ItemRepository itemRepository, CategoryRepository categoryRepository,
        UserService userService) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public ItemResponse getItemById(Long id, Long userId) {
        Item item = findItemById(id);
        boolean isWish = false;
        if (userId != 0) {
            List<WishListResponse> wishes = userService.getWishListFromUser(userId);
            isWish = wishes.stream().anyMatch(o -> o.getProductId().equals(item.getId()));
        }
        return new ItemResponse(item.getId(), item.getName(), item.getPrice(), item.getImgUrl(),
            isWish);
    }


    @Transactional
    public Long insertItem(ItemDTO itemDTO, List<OptionDTO> options)
        throws CustomDuplicateException {
        Category category = categoryRepository.findById(itemDTO.getCategoryId())
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        Item item = new Item(itemDTO.getName(), itemDTO.getPrice(), itemDTO.getImgUrl(), category);

        if (validateOptions(options)) {
            throw new CustomDuplicateException(ErrorCode.DUPLICATE_NAME);
        }

        List<Option> optionList = options.stream()
            .map(o -> new Option(o.getName(), o.getQuantity(), item)).toList();
        item.addOptionList(optionList);

        return itemRepository.save(item).getId();
    }

    @Transactional
    public Long insertOption(Long itemId, OptionDTO optionDTO)
        throws CustomDuplicateException {
        Item item = findItemById(itemId);
        item.checkDuplicateOptionName(optionDTO.getName());
        Option option = new Option(optionDTO.getName(), optionDTO.getQuantity(), item);
        item.addOption(option);
        return itemId;
    }

    @Transactional(readOnly = true)
    public Page<ItemResponse> getList(Pageable pageable, Long userId) {
        Page<Item> list = itemRepository.findAll(pageable);
        return isIncludeWishItems(userId, list);
    }

    @Transactional(readOnly = true)
    public Page<ItemResponse> getListByCategoryId(Long categoryId, Pageable pageable, Long userId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CustomNotFoundException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        Page<Item> list = itemRepository.findAllByCategoryId(categoryId, pageable);
        return isIncludeWishItems(userId, list);
    }

    private Page<ItemResponse> isIncludeWishItems(Long userId, Page<Item> list) {
        if (userId != 0) {
            List<WishListResponse> wishes = userService.getWishListFromUser(userId);
            return list.map(
                o -> new ItemResponse(o.getId(), o.getName(), o.getPrice(), o.getImgUrl(),
                    wishes.stream().anyMatch(w -> w.getProductId().equals(o.getId()))));
        }
        return list.map(o -> new ItemResponse(o.getId(), o.getName(), o.getPrice(), o.getImgUrl()));
    }

    @Transactional
    public Long updateItem(ItemDTO itemDTO) {
        Category category = categoryRepository.findById(itemDTO.getCategoryId())
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
        Item item = findItemById(itemDTO.getId());
        item.update(itemDTO, category);
        return item.getId();
    }

    @Transactional
    public Long updateOption(Long itemId, OptionDTO optionDTO)
        throws CustomArgumentNotValidException {
        Item item = findItemById(itemId);
        Option option = item.getOptionByOptionId(optionDTO.getId());
        option.update(optionDTO.getName(), optionDTO.getQuantity());
        return itemId;
    }

    @Transactional(readOnly = true)
    public List<OptionDTO> getOptionList(Long itemId) {
        Item item = findItemById(itemId);
        return item.getOptions().stream().map(OptionDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public OptionDTO getOption(Long itemId, Long optionId) {
        Item item = findItemById(itemId);
        return new OptionDTO(item.getOptionByOptionId(optionId));
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Transactional
    public void deleteOption(Long itemId, Long optionId) {
        Item item = findItemById(itemId);
        Option option = item.getOptionByOptionId(optionId);
        item.getOptions().remove(option);
    }

    @Transactional
    public void decreaseOptionQuantity(Long itemId, Long optionId, Long quantity) {
        Item item = itemRepository.findItemByIdWithPLock(itemId)
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.ITEM_NOT_FOUND));
        Option option = item.getOptionByOptionId(optionId);
        option.decreaseQuantity(quantity);
    }

    @Transactional(readOnly = true)
    public Item findItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new CustomNotFoundException(
            ErrorCode.ITEM_NOT_FOUND));
    }

    private boolean validateOptions(List<OptionDTO> options) {
        return options.stream()
            .map(OptionDTO::getName)
            .distinct()
            .count() != options.size();
    }

}
