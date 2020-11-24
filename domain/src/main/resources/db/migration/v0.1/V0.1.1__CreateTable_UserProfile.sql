create table user_profile (
    user_id varchar(50) primary key,
    updated datetime not null,
    version int not null
);
