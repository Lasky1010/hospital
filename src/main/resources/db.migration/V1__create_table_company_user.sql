create database if not exists hospital;
CREATE TABLE company_user
(
    id int8 NOT NULL AUTO_INCREMENT,

    login varchar(255) NOT NULL,

    CONSTRAINT company_user_pkey PRIMARY KEY (id),
    CONSTRAINT company_user_uniq_login UNIQUE (login)
);
