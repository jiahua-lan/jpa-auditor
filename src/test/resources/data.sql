INSERT INTO sys_user (id, username, password)
VALUES (1, 'lan', '$2a$10$mU84ca7/hZp0uEPNKZskCeDjy3e8IziCk9LHvne3egPaHuyuGQ1Cm');

INSERT INTO sys_role (id, name, code)
VALUES (2, 'ADMIN', 'ADMIN');

INSERT INTO sys_role (id, name, code)
VALUES (3, 'TESTER', 'TESTER');

INSERT INTO sys_group (id, name, code)
VALUES (4, 'ADMIN', 'ADMIN');

INSERT INTO sys_group (id, name, code)
VALUES (5, 'TEST_GROUP', 'TEST_GROUP');

INSERT INTO sys_user_roles (users_id, roles_id)
VALUES (1, 2);
INSERT INTO sys_user_roles (users_id, roles_id)
VALUES (1, 3);

INSERT INTO sys_user_groups (users_id, groups_id)
VALUES (1, 4);

INSERT INTO sys_user_groups (users_id, groups_id)
VALUES (1, 5);

insert into client (authorized_grant_types, client_id, scope, client_secret, id)
values ('password', 'client', 'all', '$2a$10$mU84ca7/hZp0uEPNKZskCeDjy3e8IziCk9LHvne3egPaHuyuGQ1Cm', 6);

ALTER SEQUENCE HIBERNATE_SEQUENCE RESTART WITH 7;