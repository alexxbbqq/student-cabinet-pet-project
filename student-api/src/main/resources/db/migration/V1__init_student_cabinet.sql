create table students (
    id uuid primary key,
    onec_id varchar(64) not null unique,
    full_name varchar(255) not null,
    initials varchar(16) not null,
    group_name varchar(64) not null,
    course integer not null,
    email varchar(255),
    phone varchar(64),
    birth_date varchar(64),
    faculty varchar(255),
    direction varchar(255),
    curator varchar(255),
    year_in integer,
    year_out integer,
    record_book varchar(64),
    education_form varchar(128),
    synced_at timestamp not null default current_timestamp
);

create table grades (
    id uuid primary key,
    student_id uuid not null references students(id) on delete cascade,
    onec_id varchar(64),
    subject varchar(255) not null,
    teacher varchar(255),
    type varchar(32) not null,
    type_label varchar(64) not null,
    grade_date_label varchar(64),
    grade_value varchar(32),
    status varchar(32) not null,
    synced_at timestamp not null default current_timestamp,
    unique (student_id, onec_id)
);

create table schedule_days (
    id uuid primary key,
    student_id uuid not null references students(id) on delete cascade,
    day_code varchar(16) not null,
    day_number integer,
    sort_order integer not null,
    synced_at timestamp not null default current_timestamp,
    unique (student_id, day_code)
);

create table lessons (
    id uuid primary key,
    schedule_day_id uuid not null references schedule_days(id) on delete cascade,
    lesson_time varchar(64) not null,
    name varchar(255) not null,
    room varchar(128),
    type varchar(32),
    sort_order integer not null
);

create table debts (
    id uuid primary key,
    student_id uuid not null references students(id) on delete cascade,
    onec_id varchar(64),
    subject varchar(255) not null,
    teacher varchar(255),
    reason varchar(255),
    retake_date varchar(128),
    location varchar(128),
    synced_at timestamp not null default current_timestamp,
    unique (student_id, onec_id)
);

create index idx_grades_student_id on grades(student_id);
create index idx_schedule_days_student_id on schedule_days(student_id);
create index idx_debts_student_id on debts(student_id);
