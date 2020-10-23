create table if not exists saly_user (
    id VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255),
    password VARCHAR(255)
);

create table if not exists roles (
    role VARCHAR(255) not null unique,
    user_id VARCHAR(255) not null,
    CONSTRAINT roles_user_fk1 FOREIGN KEY (user_id) REFERENCES saly_user(id)
);


create table if not exists customer (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255),
    last_name VARCHAR(255),
    user_id VARCHAR(255) not null,
    CONSTRAINT customer_role_fk1 FOREIGN KEY (user_id) REFERENCES saly_user(id)
)