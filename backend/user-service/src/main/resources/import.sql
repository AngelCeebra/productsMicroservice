INSERT INTO users (username, password, enabled, name, last_name, email) VALUES ('angelceebra','123456', 1,'Angel', 'Cisneros', 'angel@gmail.com');
INSERT INTO users (username, password, enabled, name, last_name, email) VALUES ('fercho','098765', 1, 'Fernando', 'Cisneros', 'fercho@gmail.com');

INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users_to_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO users_to_roles (user_id, role_id) VALUES (2, 2);
INSERT INTO users_to_roles (user_id, role_id) VALUES (2, 1);