package gift.initializer;

import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitialData implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    public InitialData(ProductRepository productRepository, CategoryRepository categoryRepository, MemberRepository memberRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {
        initialCategory();
        initialProduct();
        initialMember();
    }

    private void initialCategory() {
        categoryRepository.save(new Category("교환권", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("상품권", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("뷰티", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("패션", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("리빙/도서", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("레저/스포츠", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("아티스트/캐릭터", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("유아동/반려", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("디지털/가전", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("카카오프렌즈", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("트랜드 선물", "color", "imageUrl", "description"));
        categoryRepository.save(new Category("백화점", "color", "imageUrl", "description"));
    }

    private void initialProduct() {

        productRepository.save(new Product(
                "Ice Americano",
                4500,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
                categoryRepository.getReferenceById(1L),
                List.of(
                        new Option("Tall", 1000),
                        new Option("Small", 123),
                        new Option("Grande", 500))));
        productRepository.save(new Product(
                "Latte",
                5500,
                "https://cdn.pixabay.com/photo/2023/07/08/13/17/coffee-8114518_1280.png",
                categoryRepository.getReferenceById(1L),
                List.of(
                        new Option("Tall", 12),
                        new Option("Grande", 4))));
        productRepository.save(new Product(
                "Chair",
                150000,
                "https://i5.walmartimages.com/seo/Ktaxon-Modern-Single-Sofa-Chair-Club-Chairs-with-Side-Bags-Fabric-Arm-Chair-with-Wood-Legs-for-Living-Room-Bed-Room-Navy-Blue_088241ad-b15b-459c-9555-ef39fb140029.ac38734266e6fcb1f82674b61a537aca.jpeg",
                categoryRepository.getReferenceById(5L),
                List.of(
                        new Option("Big", 99939),
                        new Option("Small", 1))));
        productRepository.save(new Product(
                "Ryan Figure",
                10000,
                "https://img.danawa.com/prod_img/500000/156/603/img/7603156_1.jpg?_v=20200720155431",
                categoryRepository.getReferenceById(10L),
                List.of(
                        new Option("Plastic", 99939),
                        new Option("Steel", 434535))));
    }

    private void initialMember() {

        memberRepository.save(new Member("tjdgns5506@gmail.com", "1234"));
        memberRepository.save(new Member("jeungmin0724@naver.com", "1234"));
    }


}
