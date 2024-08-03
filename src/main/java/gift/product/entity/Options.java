package gift.product.entity;

import gift.option.entity.Option;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

// option을 관리할 일급 컬렉션. 이름은 Options지만 Product에 속함.
// 동일 상품 내의 옵션 이름 중복을 Product Entity에서 관리하지 않도록 만들었습니다.
@Embeddable
public class Options {

    @OneToMany
    private final List<Option> options;

    public Options(List<Option> options) {
        verifyOptionsNotEmpty(options);
        verifyDuplication(options);
        this.options = options;
    }

    public Options(Option option) {
        options = new ArrayList<>();
        options.add(option);
    }

    protected Options() {
        options = new ArrayList<>();
    }

    public List<Option> toList() {
        return options;
    }

    // 관리자의 옵션 추가
    public void addNewOption(Option newOption, long productId) {
        verifyDuplication(newOption);
        verifyOwnership(newOption, productId);

        options.add(newOption);
    }

    // 요소 검색
    public Option getOption(long optionId) {
        System.out.println(optionId);
        System.out.println(options.get(0));
        return options.stream().filter(option -> option.getOptionId() == optionId).findFirst()
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 옵션입니다."));
    }

    // 요소를 추가할 때 사용할 중복 검사.
    private void verifyDuplication(Option newOption) {
        boolean alreadyExists = options.contains(newOption);

        if (alreadyExists) {
            throw new IllegalArgumentException("중복된 옵션명입니다.");
        }
    }

    // 생성자에서 사용할 중복 검사
    private void verifyDuplication(List<Option> options) {
        Set<String> optionNameSet = options.stream().map(Option::getName).collect(
            Collectors.toSet());

        // 이름만 넣은 set의 크기와 options의 크기가 다르다면 중복된 요소가 있었을 것
        if (options.size() != optionNameSet.size()) {
            throw new IllegalArgumentException("중복된 옵션명이 존재합니다.");
        }
    }

    // 옵션을 추가할 때, 옵션이 해당하는 제품의 것인지 검사
    private void verifyOwnership(Option option, long productId) {
        if (option.getproductId() != productId) {
            throw new IllegalArgumentException("추가하려는 옵션은 해당 제품의 것이 아닙니다.");
        }
    }

    // 옵션이 비어 있으면 안되므로 검사
    private void verifyOptionsNotEmpty(List<Option> options) {
        if (options.isEmpty()) {
            throw new IllegalArgumentException("최소 하나 이상의 옵션이 있어야 합니다.");
        }
    }
}
