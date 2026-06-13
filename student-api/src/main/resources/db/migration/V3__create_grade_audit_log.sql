create table grade_audit_log (
    id uuid primary key,
    student_onec_id varchar(64) not null,
    grade_onec_id varchar(64) not null,
    subject varchar(255) not null,
    old_value varchar(32),
    new_value varchar(32),
    old_status varchar(32),
    new_status varchar(32),
    changed_at timestamp not null
);

create index idx_grade_audit_log_student_onec_id on grade_audit_log(student_onec_id);
