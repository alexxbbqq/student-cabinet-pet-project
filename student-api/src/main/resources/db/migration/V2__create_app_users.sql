create table app_users (
    id uuid primary key,
    login varchar(128) not null unique,
    password_hash varchar(255) not null,
    role varchar(32) not null,
    student_onec_id varchar(64),
    active boolean not null default true,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);

create index idx_app_users_login on app_users(login);

insert into app_users (
    id,
    login,
    password_hash,
    role,
    student_onec_id,
    active
) values (
    '11111111-1111-1111-1111-111111111111',
    '2021-301-047',
    '$2a$10$NaNrN9Aqd09Oa.VC8zW8bOjw21iPhvv7nyCt4uNrbc.lgSMeeoGcq',
    'STUDENT',
    '2021-301-047',
    true
), (
    '22222222-2222-2222-2222-222222222222',
    '2021-301-048',
    '$2a$10$NaNrN9Aqd09Oa.VC8zW8bOjw21iPhvv7nyCt4uNrbc.lgSMeeoGcq',
    'STUDENT',
    '2021-301-048',
    true
), (
    '33333333-3333-3333-3333-333333333333',
    '2022-402-015',
    '$2a$10$NaNrN9Aqd09Oa.VC8zW8bOjw21iPhvv7nyCt4uNrbc.lgSMeeoGcq',
    'STUDENT',
    '2022-402-015',
    true
), (
    '44444444-4444-4444-4444-444444444444',
    '2020-104-033',
    '$2a$10$NaNrN9Aqd09Oa.VC8zW8bOjw21iPhvv7nyCt4uNrbc.lgSMeeoGcq',
    'STUDENT',
    '2020-104-033',
    true
);
