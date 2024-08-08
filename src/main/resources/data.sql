CREATE TABLE users
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE orders
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    status       VARCHAR(50),
    total_price DECIMAL(10, 2),
    user_id      BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE order_items
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    item     VARCHAR(255),
    FOREIGN KEY (order_id) REFERENCES orders (id)
);

INSERT INTO users (name, email)
VALUES ('Олег', 'oleg@mail.ru');
INSERT INTO users (name, email)
VALUES ('Кирилл', 'kir@yandex.ru');

INSERT INTO orders (status, total_price, user_id)
VALUES ('Ожидание', 100.00, 1);
INSERT INTO orders (status, total_price, user_id)
VALUES ('Получен', 600.00, 2);
INSERT INTO orders (status, total_price, user_id)
VALUES ('Доставка', 500.00, 1);
INSERT INTO orders (status, total_price, user_id)
VALUES ('Получен', 1000.00, 2);

INSERT INTO order_items (order_id, item)
VALUES (1, 'Молоко');
INSERT INTO order_items (order_id, item)
VALUES (1, 'Картошка');
INSERT INTO order_items (order_id, item)
VALUES (2, 'Помидоры');
INSERT INTO order_items (order_id, item)
VALUES (3, 'Картошка');
INSERT INTO order_items (order_id, item)
VALUES (3, 'Лук');
INSERT INTO order_items (order_id, item)
VALUES (4, 'Картошка');