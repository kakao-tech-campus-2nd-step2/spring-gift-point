package gift.doamin.product.entity;

import java.util.List;
import java.util.stream.Collectors;

public class Options {

    private List<Option> optionList;

    public Options(List<Option> optionList) {
        if (optionList.isEmpty()) {
            throw new IllegalArgumentException("상품의 옵션은 1개 이상이어야 합니다.");
        }

        if (optionList.stream()
            .map(Option::getName)
            .collect(Collectors.toSet())
            .size() != optionList.size()) {
            throw new IllegalArgumentException("옵션 이름은 중복될 수 없습니다.");
        }

        this.optionList = optionList;
    }

    public List<Option> toList() {
        return optionList;
    }

}
