create sequence if not exists api_user_seq start with 1 increment by 1;
create sequence if not exists api_session_seq start with 1 increment by 1;
CREATE MEMORY TABLE if not exists PUBLIC.API_USER(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.api_user_seq) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.api_user_seq PRIMARY KEY,
    PASSWORD VARCHAR(250),
    PHONE VARCHAR(25),
    SALT VARCHAR(250),
    USERNAME VARCHAR(25) UNIQUE
);
CREATE MEMORY TABLE if not exists PUBLIC.API_SESSION(
    ID BIGINT DEFAULT (NEXT VALUE FOR PUBLIC.api_session_seq) NOT NULL NULL_TO_DEFAULT SEQUENCE PUBLIC.api_session_seq PRIMARY KEY,
    ACTIVE TIMESTAMP,
    ENDED TIMESTAMP,
    STARTED TIMESTAMP,
    TOKEN VARCHAR(255),
    USER_ID BIGINT
);
alter table api_session add constraint fk_session_user foreign key (user_id) references api_user;
create or replace view active_users as select distinct rownum AS ID, u.username, u.phone from api_user u join api_session s on s.user_id = u.id and s.ended is null;
create or replace view last_users as select rownum AS ID, count(u.*) active from api_user u join api_session s on s.user_id = u.id and datediff('MINUTE',now(),s.active) <= 5;