INSERT INTO user (id, name, password) VALUES (1, 'lan', '$2a$10$mU84ca7/hZp0uEPNKZskCeDjy3e8IziCk9LHvne3egPaHuyuGQ1Cm');

INSERT INTO role (id, name,code) VALUES (2, 'ADMIN','ADMIN');

INSERT INTO user_roles (users_id, roles_id) VALUES (1, 2);

ALTER SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 3;