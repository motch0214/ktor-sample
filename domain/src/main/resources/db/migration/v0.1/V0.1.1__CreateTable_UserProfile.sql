create table user_profile (
    user_id varchar(50) primary key,
    name varchar(1000) not null,
    updated datetime not null,
    version int not null
);
