create sequence hibernate_sequence start 1 increment 1;

create table comments (
    id int8 not null,
    createdDate timestamp,
    lastUpdatedDate timestamp,
    text varchar(512),
    author_id int8,
    post_id int8,
    primary key (id)
);

create table posts (
    id int8 not null,
    createdDate timestamp,
    lastUpdatedDate timestamp,
    text varchar(16384) not null,
    topic varchar(256) not null,
    author_id int8,
    primary key (id)
);

create table posts_tags (
    Post_id int8 not null,
    tags_id int8 not null,
    primary key (Post_id, tags_id)
);

create table roles (
    id int8 not null,
    name varchar(255) not null,
    primary key (id)
);

create table subscriptions (
    id int8 not null,
    createdDate timestamp,
    follow_id int8 not null,
    follower_id int8 not null,
    primary key (id)
);

create table tags (
    id int8 not null,
    name varchar(255) not null,
    primary key (id)
);

create table users (
    id int8 not null,
    birthday timestamp,
    createdDate timestamp,
    email varchar(256) not null,
    name varchar(256),
    password varchar(256) not null,
    privateStatus boolean not null,
    surname varchar(256),
    username varchar(128) not null,
    primary key (id)
);

create table users_roles (
    User_id int8 not null,
    roles_id int8 not null,
    primary key (User_id, roles_id)
);

alter table if exists comments
    add constraint comments_user_fk
    foreign key (author_id) references users;

alter table if exists comments
    add constraint comments_posts_fk
    foreign key (post_id) references posts;

alter table if exists posts
    add constraint posts_user_fk
    foreign key (author_id) references users;

alter table if exists posts_tags
    add constraint posts_tags_fk
    foreign key (tags_id) references tags;

alter table if exists posts_tags
    add constraint posts_tags_post_fk
    foreign key (Post_id) references posts;

alter table if exists subscriptions
    add constraint subscriptions_follow_fk
    foreign key (follow_id) references users;
alter table if exists subscriptions
    add constraint subscriptions_follower_fk
    foreign key (follower_id) references users;

alter table if exists users_roles
    add constraint users_roles_roles_fk
    foreign key (roles_id) references roles;

alter table if exists users_roles
    add constraint users_roles_user_fk
    foreign key (User_id) references users;
