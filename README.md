# otocyon-foxshop-backend

INSERT INTO category (name) VALUE ("laptops");
INSERT INTO category (name) VALUE ("mobiles");
INSERT INTO condition_fox (name) VALUE ("good");
INSERT INTO condition_fox (name) VALUE ("bad");
INSERT INTO location (name) VALUE ("Prague");
INSERT INTO location (name) VALUE ("Brno");

INSERT INTO delivery_method (name) VALUE ("per avion");
INSERT INTO delivery_method (name) VALUE ("PPL");

INSERT INTO role (roleName) VALUE ("VISITOR");
INSERT INTO role (id, role_name) VALUES (1, 'VISITOR');
INSERT INTO role (id, role_name) VALUES (2, 'USER');
INSERT INTO role (id, role_name) VALUES (3, 'ADMIN');
INSERT INTO role (id, role_name) VALUES (4, 'DEVELOPER');