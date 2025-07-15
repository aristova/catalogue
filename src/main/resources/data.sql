DELETE
FROM product;
ALTER TABLE product
    ALTER COLUMN id RESTART WITH 1;

DELETE
FROM review;
ALTER TABLE review
    ALTER COLUMN id RESTART WITH 1;

DELETE
FROM users;
ALTER TABLE users
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO product (title, details, image)
VALUES ('Coffee Mug', 'A ceramic mug for hot beverages', 'uploads/mug.jpeg'),
       ('Wireless Mouse', 'Ergonomic, USB, black', 'uploads/mouse.jpeg'),
       ('Notebook', '200 pages, lined paper', 'uploads/notebook.jpeg');

INSERT INTO review (text, product_id)
VALUES ('Really nice coffee mug', 1),
       ('Good', 1),
       ('The mouse does not work', 2);

INSERT INTO users (username, password)
VALUES ('admin', '{noop}password');