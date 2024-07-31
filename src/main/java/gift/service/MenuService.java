package gift.service;

import gift.domain.Menu;
import gift.domain.MenuRequest;
import gift.domain.MenuResponse;
import gift.domain.Option;
import gift.repository.CategoryRepository;
import gift.repository.MenuRepository;
import gift.repository.OptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    public MenuService(MenuRepository menuRepository, CategoryRepository categoryRepository, OptionRepository optionRepository) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
        this.optionRepository = optionRepository;
    }

    public MenuResponse save(MenuRequest request) {
        Menu menu = mapMenuRequestToMenu(request);
        return mapMenuToMenuResponse(menuRepository.save(menu));
    }

    public List<MenuResponse> findall(
            Pageable pageable
    ) {
        Page<Menu> menus = menuRepository.findAll(pageable);
        return menus.stream()
                .map(this::mapMenuToMenuResponse)
                .collect(Collectors.toList());
    }

    public MenuResponse findById(Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴 정보가 없습니다."));
        return mapMenuToMenuResponse(menu);
    }

    public MenuResponse update(Long id, MenuRequest menuRequest) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴 정보가 없습니다."));

        menu.update(new Menu(id, menuRequest,
                categoryRepository.findById(menuRequest.categoryId())
                        .orElseThrow(() -> new NoSuchElementException("해당하는 카테고리가 존재하지 않습니다."))));
        return mapMenuToMenuResponse(menuRepository.save(menu));
    }

    public void delete(Long id) {
        menuRepository.deleteById(id);
    }

    public Set<Option> getOptions(Long id) {
        return menuRepository.getOptionsByMenuId(id);
    }

    public Menu mapMenuRequestToMenu(MenuRequest menuRequest) {
        return new Menu(menuRequest.name(), menuRequest.price(), menuRequest.imageUrl(),categoryRepository.findById(menuRequest.categoryId()).get(),new HashSet<>());
    }

    public MenuResponse mapMenuToMenuResponse(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getImageUrl(), menu.getCategory(),menu.getOptions());
    }

}
