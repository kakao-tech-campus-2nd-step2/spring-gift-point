package gift.initializer;

import gift.entity.Option;
import gift.entity.OptionName;
import gift.repository.OptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class OptionDataInitializer implements CommandLineRunner {

    private final OptionRepository optionRepository;

    public OptionDataInitializer(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public void run(String... args) {
        if (optionRepository.count() == 0) {
            List<Option> options = Arrays.asList(
                    new Option(new OptionName("S")),
                    new Option(new OptionName("M")),
                    new Option(new OptionName("L")),
                    new Option(new OptionName("XL")),
                    new Option(new OptionName("빨강")),
                    new Option(new OptionName("파랑")),
                    new Option(new OptionName("초록")),
                    new Option(new OptionName("검정")),
                    new Option(new OptionName("흰"))
            );
            optionRepository.saveAll(options);
        }
    }
}
