Insert Into category(name, color, image_url)
values ('교환권', '#000000', '');
Insert Into category(name, color, image_url)
values ('상품권', '#000000', '');
Insert Into category(name, color, image_url)
values ('뷰티', '#000000', '');
Insert Into category(name, color, image_url)
values ('패션', '#000000', '');
Insert Into category(name, color, image_url)
values ('식품', '#000000', '');

INSERT INTO users(email, name, password, role)
values ('admin@admin.com', 'admin',
        '{bcrypt}$2a$10$Kd3Tjp96Ox4L6PTNpJ2byuDKRRUdFnJl89a85O8mTFzMQkyTowKFS', 'ADMIN')