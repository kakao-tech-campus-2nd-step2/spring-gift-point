INSERT INTO Category (name, img_url, description) VALUES
                                                      ('전자기기', 'https://images.unsplash.com/photo-1498049794561-7780e7231661', '최신 전자제품 및 가전제품'),
                                                      ('의류', 'https://images.unsplash.com/photo-1567401893414-76b7b1e5a7a5', '남녀노소를 위한 다양한 의류'),
                                                      ('도서', 'https://images.unsplash.com/photo-1507842217343-583bb7270b66', '베스트셀러부터 전문서적까지'),
                                                      ('식품', 'https://images.unsplash.com/photo-1506084868230-bb9d95c24759', '신선한 식재료와 가공식품'),
                                                      ('가구', 'https://images.unsplash.com/photo-1538688525198-9b88f6f53126', '집과 사무실을 위한 모던한 가구');

INSERT INTO Item (name, price, img_url, category_id) VALUES
                                                         ('스마트폰', 1000000, 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9', 1),
                                                         ('노트북', 1500000, 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853', 1),
                                                         ('청바지', 50000, 'https://images.unsplash.com/photo-1542272604-787c3835535d', 2),
                                                         ('티셔츠', 30000, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab', 2),
                                                         ('소설책', 15000, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c', 3),
                                                         ('과학책', 20000, 'https://images.unsplash.com/photo-1532012197267-da84d127e765', 3),
                                                         ('초콜릿', 5000, 'https://images.unsplash.com/photo-1511381939415-e44015466834', 4),
                                                         ('커피', 3000, 'https://images.unsplash.com/photo-1497515114629-f71d768fd07c', 4),
                                                         ('소파', 500000, 'https://images.unsplash.com/photo-1555041469-a586c61ea9bc', 5),
                                                         ('책상', 200000, 'https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd', 5);

INSERT INTO Option (name, quantity, item_id) VALUES
                                                 ('블랙', 100, 1),
                                                 ('화이트', 50, 1),
                                                 ('8GB RAM', 30, 2),
                                                 ('16GB RAM', 20, 2),
                                                 ('28인치', 50, 3),
                                                 ('30인치', 30, 3),
                                                 ('S 사이즈', 100, 4),
                                                 ('M 사이즈', 100, 4),
                                                 ('L 사이즈', 100, 4),
                                                 ('하드커버', 50, 5),
                                                 ('소프트커버', 100, 5),
                                                 ('일반판', 200, 6),
                                                 ('특별판', 50, 6),
                                                 ('다크 초콜릿', 100, 7),
                                                 ('밀크 초콜릿', 100, 7),
                                                 ('아라비카', 200, 8),
                                                 ('로부스타', 100, 8),
                                                 ('가죽 소파', 20, 9),
                                                 ('패브릭 소파', 30, 9),
                                                 ('원목 책상', 15, 10),
                                                 ('철제 책상', 25, 10);