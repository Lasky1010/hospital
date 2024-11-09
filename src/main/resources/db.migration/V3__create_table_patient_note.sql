CREATE TABLE patient_note (
                              id int8 NOT NULL AUTO_INCREMENT,
                              created_date_time timestamp NOT NULL,
                              last_modified_date_time timestamp NOT NULL,
                              created_by_user_id int8 NULL,
                              last_modified_by_user_id int8 NULL,
                              note varchar(4000) NULL,
                              patient_id int8 NOT NULL,

                              CONSTRAINT patient_note_pkey PRIMARY KEY (id)
);
ALTER TABLE patient_note ADD CONSTRAINT fk_pat_note_to_modifyed_user FOREIGN KEY (last_modified_by_user_id) REFERENCES company_user(id);
ALTER TABLE patient_note ADD CONSTRAINT fkpat_note_to_created_user FOREIGN KEY (created_by_user_id) REFERENCES company_user(id);
ALTER TABLE patient_note ADD CONSTRAINT fk_pat_note_to_patient FOREIGN KEY (patient_id) REFERENCES patient_profile(id);
