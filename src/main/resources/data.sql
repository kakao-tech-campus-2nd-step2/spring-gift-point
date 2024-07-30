INSERT INTO category (name, color, imageUrl, description)
VALUES ('교환권', '#FF5733', 'http://example.com/exchange.jpg', '설명1'),
       ('상품권', '#33FF57', 'http://example.com/giftcard.jpg', '설명2'),
       ('뷰티', '#3357FF', 'http://example.com/beauty.jpg', '설명3'),
       ('패션', '#FF33A5', 'http://example.com/fashion.jpg', '설명4'),
       ('식품', '#33FFA5', 'http://example.com/food.jpg', '설명5'),
       ('리빙/도서', '#FFA533', 'http://example.com/living_books.jpg', '설명6'),
       ('레저/스포츠', '#A533FF', 'http://example.com/leisure_sports.jpg', '설명7'),
       ('아티스트/캐릭터', '#FF33FF', 'http://example.com/artist_character.jpg', '설명8'),
       ('유아동/반려', '#FFA5FF', 'http://example.com/kids_pets.jpg', '설명9'),
       ('디지털/가전', '#33FFA5', 'http://example.com/digital_appliances.jpg', '설명10'),
       ('카카오프렌즈', '#FF5733', 'http://example.com/kakao_friends.jpg', '설명11'),
       ('트렌드선물', '#5733FF', 'http://example.com/trend_gifts.jpg', '설명12'),
       ('백화점', '#33FF57', 'http://example.com/department_store.jpg', '설명13'),
       ('홈/가전', '#FF5733', 'http://example.com/home_appliances.jpg', '설명14'),
       ('커피/디저트', '#33FF57', 'http://example.com/coffee_dessert.jpg', '설명15'),
       ('도서/음반', '#3357FF', 'http://example.com/books_music.jpg', '설명16'),
       ('여행/숙박', '#FF33A5', 'http://example.com/travel_accommodation.jpg', '설명17'),
       ('건강/뷰티', '#33FFA5', 'http://example.com/health_beauty.jpg', '설명18'),
       ('자동차/공구', '#FFA533', 'http://example.com/automotive_tools.jpg', '설명19'),
       ('화장품/향수', '#A533FF', 'http://example.com/cosmetics_perfume.jpg', '설명20'),
       ('완구/취미', '#FF33FF', 'http://example.com/toys_hobbies.jpg', '설명21'),
       ('음식점', '#FFA5FF', 'http://example.com/restaurants.jpg', '설명22'),
       ('의류/잡화', '#33FFA5', 'http://example.com/clothing_accessories.jpg', '설명23'),
       ('반려동물', '#FF5733', 'http://example.com/pets.jpg', '설명24'),
       ('헬스/피트니스', '#33FF57', 'http://example.com/health_fitness.jpg', '설명25'),
       ('전자기기', '#3357FF', 'http://example.com/electronics.jpg', '설명26'),
       ('가구/인테리어', '#FF33A5', 'http://example.com/furniture_interior.jpg', '설명27'),
       ('액세서리', '#33FFA5', 'http://example.com/accessories.jpg', '설명28'),
       ('주얼리', '#FFA533', 'http://example.com/jewelry.jpg', '설명29'),
       ('가전제품', '#A533FF', 'http://example.com/appliances.jpg', '설명30');

INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('coffee', 1500, 'image1', 15);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('meat', 15500, 'image2', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('egg', 3000, 'image3', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('soup', 40000, 'image4', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('bread', 2500, 'image5', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('milk', 1000, 'image6', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('cheese', 7000, 'image7', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('chocolate', 3500, 'image8', 15);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('wine', 45000, 'image9', 15);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('beer', 2000, 'image10', 15);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('juice', 1200, 'image11', 15);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('water', 500, 'image12', 15);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('soda', 800, 'image13', 15);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('apple', 300, 'image14', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('banana', 600, 'image15', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('grape', 2500, 'image16', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('peach', 1500, 'image17', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('pineapple', 3500, 'image18', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('mango', 4000, 'image19', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('strawberry', 3000, 'image20', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('blueberry', 5000, 'image21', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('raspberry', 6000, 'image22', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('blackberry', 5500, 'image23', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('orange', 2000, 'image24', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('lemon', 1000, 'image25', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('lime', 1100, 'image26', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('avocado', 7000, 'image27', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('broccoli', 2000, 'image28', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('carrot', 1000, 'image29', 5);
INSERT INTO product (name, price, imageUrl, category_id)
VALUES ('tomato', 1500, 'image30', 5);

INSERT INTO product_option (name, quantity, product_id)
VALUES ('Americano', 10, 1),
       ('Latte', 5, 1),
       ('ColdBrew', 20, 1);

INSERT INTO users (email, password, login_type)
VALUES ('admin@email.com', 'password', 'DEFAULT');

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


