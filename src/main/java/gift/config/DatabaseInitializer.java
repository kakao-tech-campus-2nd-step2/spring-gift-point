package gift.config;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initDatabase(CategoryRepository categoryRepository, ProductRepository productRepository, OptionRepository optionRepository) {
        return args -> {
            Category category = new Category("Electronics");
            categoryRepository.save(category);

            Product product1 = new Product("Smartphone", 500, "image_url_1", category);
            Product product2 = new Product("Laptop", 1000, "image_url_2", category);

            productRepository.save(product1);
            productRepository.save(product2);

            Option option1 = new Option("64GB", 100, product1);
            Option option2 = new Option("128GB", 200, product1);
            Option option3 = new Option("256GB", 150, product2);
            Option option4 = new Option("512GB", 250, product2);

            optionRepository.save(option1);
            optionRepository.save(option2);
            optionRepository.save(option3);
            optionRepository.save(option4);
        };
    }
}
