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
        categoryRepository.save(new Category("교환권", "#FEE500", "https://images.unsplash.com/photo-1535241556859-780cb9f395f2?q=80&w=2825&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "교환권임"));
        categoryRepository.save(new Category("상품권", "#FEE500", "https://images.unsplash.com/photo-1526336024174-e58f5cdd8e13?q=80&w=2787&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "상품권임"));
        categoryRepository.save(new Category("뷰티", "#FEE500", "https://images.unsplash.com/photo-1477764250597-dffe9f601ae8?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "뷰티임"));
        categoryRepository.save(new Category("패션", "#FEE500", "https://images.unsplash.com/photo-1517849845537-4d257902454a?q=80&w=2835&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "패션임"));
        categoryRepository.save(new Category("리빙/도서", "#FEE500", "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?q=80&w=2946&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "리빙,도서임"));
        categoryRepository.save(new Category("레저/스포츠", "#FEE500", "https://plus.unsplash.com/premium_photo-1684820878202-52781d8e0ea9?q=80&w=2942&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "레저,스포츠임"));
        categoryRepository.save(new Category("아티스트/캐릭터", "#FEE500", "https://plus.unsplash.com/premium_vector-1721995308967-c7802e7361c9?q=80&w=3176&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "아티스트,캐릭터임"));
        categoryRepository.save(new Category("유아동/반려", "#FEE500", "https://plus.unsplash.com/premium_vector-1682270497445-1d30983b683d?q=80&w=2916&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "유아동,반려임"));
        categoryRepository.save(new Category("디지털/가전", "#FEE500", "https://plus.unsplash.com/premium_vector-1716910449787-ce65ae43d54e?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "디지털,가전임"));
        categoryRepository.save(new Category("카카오프렌즈", "#FEE500", "https://plus.unsplash.com/premium_vector-1720833276380-436f053a77bd?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "카카오프렌즈"));
        categoryRepository.save(new Category("트랜드 선물", "#FEE500", "https://plus.unsplash.com/premium_vector-1701795462492-d97da6d52d73?q=80&w=2960&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "트렌드 선물"));
        categoryRepository.save(new Category("백화점", "#FEE500", "https://plus.unsplash.com/premium_vector-1683121609098-3a7345febc0b?q=80&w=2716&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "백화점"));
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
