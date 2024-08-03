package gift.model.valueObject;

import gift.model.entity.Option;

import java.util.List;

public class OptionList {
    private final List<Option> options;

    public OptionList(List<Option> options) {
        this.options = options;
    }

    public boolean canDelete() {
        if (this.options.size() >= 2) {
            return true;
        }
        throw new IllegalArgumentException("옵션은 한개 이상 존재해야 합니다.");
    }

    public boolean isContains(Option option) {
        if (this.options.contains(option)) {
            throw new IllegalArgumentException("옵션은 중복될 수 없습니다.");
        }
        return false;
    }
}
