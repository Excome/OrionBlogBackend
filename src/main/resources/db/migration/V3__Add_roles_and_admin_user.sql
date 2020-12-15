insert into roles (id, name)
    values (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

insert into users (id, email, username, password, surname, name, privatestatus)
    values (3, 'admin@admin.com', 'admin',
            '$2a$10$8k0tSFbnDhe9U1w3EDiYTeCuKDD/HknMV2i2ZDelOYZHr9lzPGgPi',
            'AdminSurname',
            'AdminName',
            false);

insert into users_roles (user_id, roles_id)
    values (3, 1), (3,2);