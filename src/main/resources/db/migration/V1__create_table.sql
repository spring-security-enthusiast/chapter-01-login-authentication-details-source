create table users (
    id          uuid primary key not null,
    username    text             not null,
    tenant_id   text             not null,
    password    text             not null,
    email       text             ,
    first_name  text             ,
    last_name   text             ,
    version     int              ,
    account_non_expired boolean not null,
    account_non_expired_locked  boolean not null,
    credentials_non_expired boolean  not null,
    enabled         boolean not null,
    active      boolean default true
);
alter table users add constraint uq_users_tenant_username unique (tenant_id, username);
alter table users add constraint uq_users_tenant_email unique (tenant_id, email);



create table roles
(
    id            uuid primary key not null,
    name          text,
    version       int,
    active        boolean default true
);

create table users_roles
(
    user_id    uuid not null,
    role_id    uuid not null,
    CONSTRAINT users_roles_pk PRIMARY KEY (user_id, role_id),
    CONSTRAINT FK_users FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT FK_roles FOREIGN KEY (role_id) REFERENCES roles (id)
);