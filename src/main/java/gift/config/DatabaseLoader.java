package gift.config;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseLoader {

    @Bean
    public CommandLineRunner initData(ProductRepository repository, CategoryRepository categoryRepository) {
        return args -> {
            // 공통 옵션 생성
            Option packageOption = new Option("포장 선택", 1, 3000, null , 100);
            Option deliverOption = new Option("배송비 포함", 1, 2500, null , 100);

            // 상품 및 옵션 데이터 삽입
            Product product1 = new Product(2001L, "[기프티콘] BBQ 황금올리브치킨", 20000, "https://cdn.011st.com/11dims/resize/600x600/quality/75/11src/product/1802204067/B.jpg?315000000", 1);
            List<Option> options1 = Arrays.asList(
                new Option("[기프티콘] BBQ 황금올리브치킨", 1, 20000, product1 , 100),
                new Option("01. 치즈볼 5알", 1, 6000, product1 , 100),
                new Option(packageOption.getName(), packageOption.getQuantity(), packageOption.getPrice(), product1 ,
                    packageOption.getMaxQuantity()),
                new Option(deliverOption.getName(), deliverOption.getQuantity(), deliverOption.getPrice(), product1, deliverOption.getMaxQuantity())
            );
            product1.setOptions(options1);
            repository.save(product1);

            Product product2 = new Product(2002L, "스타벅스 기프트 카드", 0, "https://image.istarbucks.co.kr/cardImg/20200818/007633_WEB.png", 2);
            List<Option> options2 = Arrays.asList(
                new Option("스타벅스 기프트 카드", 1, 0, product2, 100),
                new Option("01. [Gift] 스타벅스 기프트 카드 10000원 ", 1, 10000, product2, 100),
                new Option("01. [Gift] 스타벅스 기프트 카드 30000원 ", 1, 30000, product2, 100),
                new Option("01. [Gift] 스타벅스 기프트 카드 50000원", 1, 50000, product2, 100),
                new Option(packageOption.getName(), packageOption.getQuantity(), packageOption.getPrice(), product2, 100),
                new Option(deliverOption.getName(), deliverOption.getQuantity(), deliverOption.getPrice(), product2, 100)
            );
            product2.setOptions(options2);
            repository.save(product2);

            Product product3 = new Product(3001L , "1025 독도 크림 80ml", 25600, "https://roundlab.co.kr/web/product/big/202207/96ab1fe05298e73355fb50b04550226b.jpg" , 3);
            List<Option> options3 = Arrays.asList(
                new Option("1025 독도 크림 80ml", 1, 25600, product3, 100),
                new Option("01. [추가] 20ml 추가 구매", 1, 6000, product3, 100),
                new Option(packageOption.getName(), packageOption.getQuantity(), packageOption.getPrice(), product3, 100),
                new Option(deliverOption.getName(), deliverOption.getQuantity(), deliverOption.getPrice(), product3, 100)
            );
            product3.setOptions(options3);
            repository.save(product3);

            Product product4 = new Product(4001L , "체크남방", 19900, "https://item.elandrs.com/upload/prd/orgimg/032/2004327032_0000001.jpg?w=1020&h=&q=100" , 4);
            List<Option> options4 = Arrays.asList(
                new Option("체크남방", 1, 19900, product4, 100),
                new Option("01. 양말 1켤래", 1, 1000, product4, 100),
                new Option(packageOption.getName(), packageOption.getQuantity(), packageOption.getPrice(), product4, 100),
                new Option(deliverOption.getName(), deliverOption.getQuantity(), deliverOption.getPrice(), product4, 100)
            );
            product4.setOptions(options4);
            repository.save(product4);

            Product product5 = new Product(6001L , "인간실격", 8100 , "https://image.yes24.com/goods/1387488/XL" , 6);
            List<Option> options5 = Arrays.asList(
                new Option("인간실격", 1, 8100, product5, 100),
                new Option("01. 책 갈피", 1, 800, product5, 100),
                new Option(packageOption.getName(), packageOption.getQuantity(), packageOption.getPrice(), product5, 100),
                new Option(deliverOption.getName(), deliverOption.getQuantity(), deliverOption.getPrice(), product5, 100)
            );
            product5.setOptions(options5);
            repository.save(product5);

            Product product6 = new Product(7001L , "윌슨 NBA 콤프 농구공", 85000 , "https://kr.wilson.com/cdn/shop/products/P_WTB7100XB_view_1_1728x.png?v=1660613945" , 7);
            List<Option> options6 = Arrays.asList(
                new Option("윌슨 NBA 콤프 농구공", 1, 85000, product6, 100),
                new Option("01. 휴대용 에어 펌프", 1, 4000, product6, 100),
                new Option(packageOption.getName(), packageOption.getQuantity(), packageOption.getPrice(), product6, 100),
                new Option(deliverOption.getName(), deliverOption.getQuantity(), deliverOption.getPrice(), product6, 100)
            );
            product6.setOptions(options6);
            repository.save(product6);

            Product product7 = new Product(10001L , "삼성 드럼 세탁기 9kg", 700000 , "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT77j3eHCybMMbV-D_xMRz0WPgReRpoSnHprA&s" , 10);
            List<Option> options7 = Arrays.asList(
                new Option("삼성 드럼 세탁기 9kg", 1, 700000, product7, 100),
                new Option("01. 설치비", 1, 40000, product7, 100),
                new Option(packageOption.getName(), packageOption.getQuantity(), packageOption.getPrice(), product7, 100),
                new Option(deliverOption.getName(), deliverOption.getQuantity(), deliverOption.getPrice(), product7, 100)
            );
            product7.setOptions(options7);
            repository.save(product7);

            Product product8 = new Product(10002L , "삼성 양문형 냉장고 846 L", 1490000 , "https://images.samsung.com/kdp/goods/2022/09/05/94432d53-fbf7-4052-a68f-a355e6957783.png?$PD_GALLERY_L_PNG$" , 10);
            List<Option> options8 = Arrays.asList(
                new Option("삼성 양문형 냉장고 846 L", 1, 1490000, product8, 100),
                new Option("01. 설치비", 1, 40000, product8, 100),
                new Option(packageOption.getName(), packageOption.getQuantity(), packageOption.getPrice(), product8, 100),
                new Option(deliverOption.getName(), deliverOption.getQuantity(), deliverOption.getPrice(), product8, 100)
            );
            product8.setOptions(options8);
            repository.save(product8);

            Product product9 = new Product(11001L , "이모티콘", 0 , "https://t1.kakaocdn.net/thumb/R1920x0.fwebp.q100/?fname=https%3A%2F%2Ft1.kakaocdn.net%2Fkakaocorp%2Fkakaocorp%2Fadmin%2Fservice%2Fa85d0594017900001.jpg" , 11);
            List<Option> options9 = Arrays.asList(
                new Option("이모티콘", 1, 0, product9, 100),
                new Option("01. 춘식이", 1, 2500, product9, 100),
                new Option("02. 라이언", 1, 2500, product9, 100),
                new Option("03. 어피이", 1, 2500, product9, 100),
                new Option(packageOption.getName(), packageOption.getQuantity(), packageOption.getPrice(), product9, 100),
                new Option(deliverOption.getName(), deliverOption.getQuantity(), deliverOption.getPrice(), product9, 100)
            );
            product9.setOptions(options9);
            repository.save(product9);

            categoryRepository.save(new Category(1, "교환권", "#6c95d1", "https://png.pngtree.com/png-vector/20230330/ourmid/pngtree-red-ticket-icon-vector-illustration-png-image_6674739.png", ""));
            categoryRepository.save(new Category(2, "상품권", "#6c95d1", "https://imagescdn.gettyimagesbank.com/500/201809/a11186062.jpg", ""));
            categoryRepository.save(new Category(3, "뷰티", "#6c95d1", "https://img.imbc.com/adams/Corner/20175/131391670413239055.jpg", ""));
            categoryRepository.save(new Category(4, "패션", "#6c95d1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKm65ftNFrjmtS-ahj9sPyZ7W_ONShws9GfQ&s", ""));
            categoryRepository.save(new Category(5, "식품", "#6c95d1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1Xh0h-1FVvS5d0Wxvj38Hu7kBs4KSpUyyPw&s", ""));
            categoryRepository.save(new Category(6, "리빙/도서", "#6c95d1", "https://i.pinimg.com/236x/38/ca/ec/38caeccc9a859f3eb845c9dc91fdcd93.jpg", ""));
            categoryRepository.save(new Category(7, "레저/스포츠", "#6c95d1", "https://aiartshop.com/cdn/shop/files/a-cat-is-surfing-on-big-wave-ai-painting-316.webp?v=1711235460", ""));
            categoryRepository.save(new Category(8, "아티스트/캐릭터", "#6c95d1", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS7CwAhw_DpRjfwSpfzUFKdP-G80zGatPK7Jg&s", ""));
            categoryRepository.save(new Category(9, "유아동/반려", "#6c95d1", "https://img1.newsis.com/2023/07/12/NISI20230712_0001313626_web.jpg", ""));
            categoryRepository.save(new Category(10, "디지털/가전", "#6c95d1", "https://img.animalplanet.co.kr/news/2020/12/18/700/m264759705v49k3nxgn9.jpg", ""));
            categoryRepository.save(new Category(11, "카카오프렌즈", "#6c95d1", "https://blog.kakaocdn.net/dn/cadWYP/btrhVsFqMqq/Q3HWGGl726G9Fs0vN93Dj0/img.jpg", ""));
            categoryRepository.save(new Category(12, "트렌드 선물", "#6c95d1", "https://cdn.hankyung.com/photo/202405/01.36897265.1.jpg", ""));
            categoryRepository.save(new Category(13, "백화점", "#6c95d1", "https://i.namu.wiki/i/uRAh-N8MXDIeQzBevIneGDBTIpZ4JLuqY2POyA-GmqdHcNUr_qjTmhwKcint-NK04dq2d03viq8CRT5cXtkKGA.webp", ""));
        };
    }
}
