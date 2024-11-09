CREATE TABLE patient_profile
(
    id         int8         NOT NULL AUTO_INCREMENT,
    first_name varchar(255) NULL,
    last_name  varchar(255) NULL,
    status_id  int2         NOT NULL,

    CONSTRAINT patient_profile_pkey PRIMARY KEY (id)

);
CREATE TABLE old_client_guids
(
    client_id       BIGINT       NOT NULL,
    old_client_guid VARCHAR(255) NOT NULL,
    PRIMARY KEY (client_id, old_client_guid),
    FOREIGN KEY (client_id) REFERENCES patient_profile (id) ON DELETE CASCADE
);

