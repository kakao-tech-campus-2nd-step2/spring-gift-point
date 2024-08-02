INSERT INTO category (name, color, imageUrl, description)
VALUES ('교환권', '#FF5733', 'https://cdn.pixabay.com/photo/2015/08/11/08/21/coupon-883642_640.png', '설명1'),
       ('상품권', '#33FF57', 'https://cdn.pixabay.com/photo/2014/10/23/10/10/dollars-499481_640.jpg', '설명2'),
       ('뷰티', '#3357FF', 'https://cdn.pixabay.com/photo/2016/05/02/17/56/lipstick-1367771_640.jpg', '설명3'),
       ('패션', '#FF33A5', 'https://cdn.pixabay.com/photo/2021/11/12/13/14/sweater-6788998_640.jpg', '설명4'),
       ('식품', '#33FFA5', 'https://cdn.pixabay.com/photo/2019/08/15/09/03/pizza-4407510_640.jpg', '설명5'),
       ('리빙/도서', '#FFA533', 'https://cdn.pixabay.com/photo/2017/08/09/10/32/reading-2614105_640.jpg', '설명6'),
       ('레저/스포츠', '#A533FF', 'https://cdn.pixabay.com/photo/2023/07/22/04/15/motorbike-8142649_640.jpg', '설명7'),
       ('아티스트/캐릭터', '#FF33FF', 'https://cdn.pixabay.com/photo/2022/08/07/08/32/tiger-7370142_640.png', '설명8'),
       ('유아동/반려', '#FFA5FF', 'https://cdn.pixabay.com/photo/2017/11/10/08/08/baby-2935722_640.jpg', '설명9'),
       ('디지털/가전', '#33FFA5', 'https://cdn.pixabay.com/photo/2018/08/09/03/58/home-3593730_640.jpg', '설명10'),
       ('카카오프렌즈', '#FF5733',
        'https://i.namu.wiki/i/vDDaVK4wm1-vPZgAOI65rbhLhr1vPCzBgoRKSS7mEFx4IH2vtHvvMN41Umw-taptksIW_WqnjwOdcGbAMpAmrQ.webp',
        '설명11'),
       ('트렌드선물', '#5733FF', 'https://cdn.pixabay.com/photo/2014/04/03/11/36/gift-311970_640.png', '설명12'),
       ('홈/가전', '#FF5733', 'https://cdn.pixabay.com/photo/2021/08/19/06/47/architecture-6557219_640.jpg', '설명14'),
       ('커피/디저트', '#33FF57', 'https://cdn.pixabay.com/photo/2019/11/11/11/35/cafe-4618121_640.jpg', '설명15'),
       ('도서/음반', '#3357FF', 'https://cdn.pixabay.com/photo/2016/11/29/01/22/album-1866523_640.jpg', '설명16'),
       ('여행/숙박', '#FF33A5', 'https://cdn.pixabay.com/photo/2020/02/27/20/48/aircraft-4885805_640.jpg', '설명17'),
       ('건강/뷰티', '#33FFA5', 'https://cdn.pixabay.com/photo/2023/08/30/09/44/woman-8222956_640.jpg', '설명18'),
       ('자동차/공구', '#FFA533', 'https://cdn.pixabay.com/photo/2024/05/10/23/34/car-8753811_640.png', '설명19');



INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('커피', 1500, 'https://via.placeholder.com/150', 14),
       ('고기', 15500, 'https://via.placeholder.com/150', 5),
       ('계란', 3000, 'https://via.placeholder.com/150', 5),
       ('수프', 40000, 'https://via.placeholder.com/150', 5),
       ('빵', 2500, 'https://via.placeholder.com/150', 5),
       ('우유', 1000, 'https://via.placeholder.com/150', 5),
       ('치즈', 7000, 'https://via.placeholder.com/150', 5),
       ('초콜릿', 3500, 'https://via.placeholder.com/150', 14),
       ('와인', 45000, 'https://via.placeholder.com/150', 14),
       ('맥주', 2000, 'https://via.placeholder.com/150', 14),
       ('주스', 1200, 'https://via.placeholder.com/150', 14),
       ('물', 500, 'https://via.placeholder.com/150', 14),
       ('소다', 800, 'https://via.placeholder.com/150', 14),
       ('사과', 300, 'https://via.placeholder.com/150', 5),
       ('바나나', 600, 'https://via.placeholder.com/150', 5),
       ('포도', 2500, 'https://via.placeholder.com/150', 5),
       ('복숭아', 1500, 'https://via.placeholder.com/150', 5),
       ('파인애플', 3500, 'https://via.placeholder.com/150', 5),
       ('망고', 4000, 'https://via.placeholder.com/150', 5),
       ('딸기', 3000, 'https://via.placeholder.com/150', 5),
       ('블루베리', 5000, 'https://via.placeholder.com/150', 5),
       ('라즈베리', 6000, 'https://via.placeholder.com/150', 5),
       ('블랙베리', 5500, 'https://via.placeholder.com/150', 5),
       ('오렌지', 2000, 'https://via.placeholder.com/150', 5),
       ('레몬', 1000, 'https://via.placeholder.com/150', 5),
       ('라임', 1100, 'https://via.placeholder.com/150', 5),
       ('아보카도', 7000, 'https://via.placeholder.com/150', 5),
       ('브로콜리', 2000, 'https://via.placeholder.com/150', 5),
       ('당근', 1000, 'https://via.placeholder.com/150', 5),
       ('토마토', 1500, 'https://via.placeholder.com/150', 5),
       ('가전교환권', 200000, 'https://via.placeholder.com/150', 1),
       ('문화상품권', 200000, 'https://via.placeholder.com/150', 2),
       ('립스틱', 20000, 'https://via.placeholder.com/150', 3);

-- 프로덕트 옵션 추가
INSERT INTO product_option (name, quantity, product_id)
VALUES ('Americano', 10, 1),
       ('Latte', 5, 1),
       ('ColdBrew', 20, 1),
       ('BeefSteak', 8, 2),
       ('PorkRibs', 12, 2),
       ('ChickenBreast', 15, 2),
       ('ScrambledEggs', 30, 3),
       ('BoiledEggs', 25, 3),
       ('Omelette', 20, 3),
       ('TomatoSoup', 5, 4),
       ('ChickenSoup', 8, 4),
       ('MushroomSoup', 6, 4),
       ('WholeWheatBread', 10, 5),
       ('MultigrainBread', 12, 5),
       ('Sourdough', 8, 5),
       ('SkimMilk', 15, 6),
       ('WholeMilk', 10, 6),
       ('AlmondMilk', 5, 6),
       ('CheddarCheese', 10, 7),
       ('MozzarellaCheese', 8, 7),
       ('ParmesanCheese', 5, 7),
       ('DarkChocolate', 15, 8),
       ('MilkChocolate', 12, 8),
       ('WhiteChocolate', 10, 8),
       ('RedWine', 6, 9),
       ('WhiteWine', 4, 9),
       ('RoseWine', 8, 9),
       ('CraftBeer', 20, 10),
       ('IPA', 15, 10),
       ('Lager', 12, 10),
       ('OrangeJuice', 25, 11),
       ('AppleJuice', 20, 11),
       ('GrapeJuice', 15, 11),
       ('SparklingWater', 30, 12),
       ('SpringWater', 25, 12),
       ('MineralWater', 20, 12),
       ('CocaCola', 50, 13),
       ('Pepsi', 40, 13),
       ('Sprite', 30, 13),
       ('GreenApple', 10, 14),
       ('RedApple', 15, 14),
       ('GoldenApple', 8, 14),
       ('Banana', 20, 15),
       ('Plantain', 15, 15),
       ('BabyBanana', 10, 15),
       ('RedGrapes', 12, 16),
       ('GreenGrapes', 10, 16),
       ('BlackGrapes', 8, 16),
       ('YellowPeach', 15, 17),
       ('WhitePeach', 10, 17),
       ('FlatPeach', 5, 17),
       ('PineappleSlices', 20, 18),
       ('PineappleChunks', 15, 18),
       ('DriedPineapple', 10, 18),
       ('MangoChunks', 25, 19),
       ('MangoPuree', 15, 19),
       ('DriedMango', 10, 19),
       ('StrawberryJam', 30, 20),
       ('FreshStrawberry', 25, 20),
       ('DriedStrawberry', 20, 20),
       ('BlueberryJam', 15, 21),
       ('FreshBlueberry', 10, 21),
       ('DriedBlueberry', 8, 21),
       ('RaspberryJam', 12, 22),
       ('FreshRaspberry', 15, 22),
       ('DriedRaspberry', 10, 22),
       ('BlackberryJam', 10, 23),
       ('FreshBlackberry', 8, 23),
       ('DriedBlackberry', 6, 23),
       ('NavelOrange', 20, 24),
       ('BloodOrange', 15, 24),
       ('MandarinOrange', 12, 24),
       ('EurekaLemon', 25, 25),
       ('MeyerLemon', 15, 25),
       ('KeyLime', 10, 25),
       ('PersianLime', 20, 26),
       ('KaffirLime', 12, 26),
       ('FingerLime', 8, 26),
       ('HassAvocado', 15, 27),
       ('FuerteAvocado', 10, 27),
       ('BaconAvocado', 5, 27),
       ('BroccoliFlorets', 20, 28),
       ('BroccoliStems', 15, 28),
       ('BroccoliCrown', 10, 28),
       ('CarrotSticks', 25, 29),
       ('BabyCarrots', 20, 29),
       ('CarrotCubes', 15, 29),
       ('CherryTomatoes', 30, 30),
       ('RomaTomatoes', 25, 30),
       ('HeirloomTomatoes', 20, 30),
       ('세탁기 교환권', 20, 31),
       ('가스레인지 교환권', 20, 31),
       ('백화점 상품권', 20, 32),
       ('마트 상품권', 20, 32),
       ('빨간 립스틱', 20, 33),
       ('파란 립스틱', 20, 33);

INSERT INTO users (email, password, name, point, role, login_type)
VALUES ('admin@test.com', '1234', 'Peach', 5000, 'ADMIN', 'DEFAULT');

INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 1, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 2, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 3, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 4, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 5, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 6, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 7, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 8, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 9, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 10, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 11, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 12, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 13, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 14, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 15, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 16, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 17, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 18, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 19, 1);
INSERT INTO wish (user_id, product_id, quantity)
VALUES (1, 20, 1);


