package gift.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Options {

    @OneToMany(mappedBy = "options", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

    public Options(List<Option> options) {
        if (options.stream()
            .map(Option::getName)
            .collect(Collectors.toSet())
            .size() != options.size()) {
            throw new IllegalArgumentException();
        }
        this.options = new ArrayList<>();
    }

    public void addOption(Option option) {

    }

    public void validate() {
        if (options.stream()
            .map(Option::getName)
            .collect(Collectors.toSet())
            .size()
            != options.size()) {
            throw new IllegalArgumentException();
        }
    }
}
