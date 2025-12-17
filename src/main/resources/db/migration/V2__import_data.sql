insert into roles (id, name, version, active) values ('39259807-1e2e-4408-ac3d-7e3a2c4c0340', 'ADMIN', 0, true);
insert into roles (id, name, version, active) values ('88dab79b-deb1-4174-81ac-c8994b5515bc', 'MANAGER', 0, true);
insert into roles (id, name, version, active) values ('276ec948-2d56-4d4c-a1dd-f5ae2720dd13', 'USER', 0, true);

insert into users (id, username, password, email, tenant_id, first_name, last_name, version, account_non_expired, account_non_expired_locked, credentials_non_expired, enabled)
values ('3bf22efb-4412-4b1c-9fc0-d3426e76cd13', 'john', '$2a$10$4A3V3Vh6ggdQNBRNYrjrMOFkEKMlb7hrUGujkgOYgw3a0Tai7STFS', 'john@test.com', 'acme',  'John', 'Robson', 0, true, true, true, true);

insert into users (id, username, password, email, tenant_id, first_name, last_name, version, account_non_expired, account_non_expired_locked, credentials_non_expired, enabled)
values ('8402a72d-5e35-46de-a52b-452306b8bf7d', 'bob', '$2a$10$4A3V3Vh6ggdQNBRNYrjrMOFkEKMlb7hrUGujkgOYgw3a0Tai7STFS', 'bob@test.com', 'globex', 'Bob', 'Benson', 0, true, true, true, true);

insert into users (id, username, password, email, tenant_id, first_name, last_name, version, account_non_expired, account_non_expired_locked, credentials_non_expired, enabled)
values ('efd5096e-ae11-4684-a83f-029fb9647d35', 'winton', '$2a$10$4A3V3Vh6ggdQNBRNYrjrMOFkEKMlb7hrUGujkgOYgw3a0Tai7STFS',  'winton@test.com', 'acme', 'Winton', 'Lucas', 0, true, true, true, true);

insert into users (id, username, password, email, tenant_id, first_name, last_name, version, account_non_expired, account_non_expired_locked, credentials_non_expired, enabled)
values ('d3aa0245-ef06-4999-9335-91e75993b6be', 'harley', '$2a$10$4A3V3Vh6ggdQNBRNYrjrMOFkEKMlb7hrUGujkgOYgw3a0Tai7STFS', 'harley@test.com', 'globex', 'Harley', 'Curry', 0, true, true, true, true);

-- assign John --
insert into users_roles (user_id, role_id) values ('3bf22efb-4412-4b1c-9fc0-d3426e76cd13','39259807-1e2e-4408-ac3d-7e3a2c4c0340');
-- assign Bob --
insert into users_roles (user_id, role_id) values ('8402a72d-5e35-46de-a52b-452306b8bf7d','88dab79b-deb1-4174-81ac-c8994b5515bc');
-- assign Winton --
insert into users_roles (user_id, role_id) values ('efd5096e-ae11-4684-a83f-029fb9647d35','39259807-1e2e-4408-ac3d-7e3a2c4c0340');
-- assign Harley --
insert into users_roles (user_id, role_id) values ('d3aa0245-ef06-4999-9335-91e75993b6be','276ec948-2d56-4d4c-a1dd-f5ae2720dd13');

