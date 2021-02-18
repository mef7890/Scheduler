CREATE TABLE IF NOT EXISTS contact(
    contact_table_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    FOREIGN KEY(student_id) REFERENCES person(person_id),
    contact_id BIGINT NOT NULL,
    FOREIGN KEY(contact_id) REFERENCES person(person_id),
    relationship VARCHAR(20) NOT NULL
    );