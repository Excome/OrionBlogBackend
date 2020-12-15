insert into roles (id, name) values (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

insert into users(id, username, privatestatus,email, password) values (1, 'user', false, 'user@user.com', '');
insert into users(id, username, privatestatus, email, password) values (2, 'test', false, '', '');

insert into users_roles(user_id, roles_id) values (1, 1);
insert into users_roles(user_id, roles_id) values (2, 1);